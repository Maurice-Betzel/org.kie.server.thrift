/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.kie.server.remote.rest.thrift.drools;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.server.remote.convert.Converter;
import org.kie.server.remote.rest.extension.ThriftMessageReader;
import org.kie.server.remote.rest.extension.ThriftMessageWriter;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.drools.RulesExecutionService;
import org.kie.server.services.impl.KieContainerInstanceImpl;
import org.kie.server.thrift.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by x3.mbetzel on 04.01.2016.
 */
@Path("server/thrift/containers/instances/{id}")
public class CommandResource {

    private static final Logger logger = LoggerFactory.getLogger(CommandResource.class);

    private KieCommands commandsFactory = KieServices.Factory.get().getCommands();
    private RulesExecutionService rulesExecutionService;
    private KieServerRegistry registry;


    public CommandResource() {
    }

    public CommandResource(RulesExecutionService rulesExecutionService, KieServerRegistry registry) {
        this.rulesExecutionService = rulesExecutionService;
        this.registry = registry;
    }

    @POST
    @Path("/{ksessionId}")
    public KieServicesResponse callContainer(@PathParam("id") String containerId, @PathParam("ksessionId") String ksessionId, KieServicesRequest kieServicesRequest) {
        List<Command<?>> commands = new ArrayList();
        BatchExecutionCommand batchExecutionCommand = kieServicesRequest.getBatchExecutionCommand();
        TDeserializer tDeserializer = ThriftMessageReader.pollTDeserializer();
        TSerializer tSerializer = ThriftMessageWriter.pollTSerializer();

        KieContainerInstanceImpl kci = registry.getContainer(containerId);
        if(kci == null) {
            logger.warn("No container deployed with id '{}'", containerId);
            return new KieServicesResponse().setResponse(Response.kieServicesException((new KieServicesException("UnknownContainerException","Unknown container: " + containerId))));
        }
        ClassLoader kieContainerClassLoader = kci.getKieContainer().getClassLoader();
        try {
            if (batchExecutionCommand.isSetInsertObjectCommands()) {
                for (InsertObjectCommand insertObjectCommand : batchExecutionCommand.getInsertObjectCommands()) {
                    Object object = Converter.ConvertCommandPayLoad(insertObjectCommand, kieContainerClassLoader);
                    org.drools.core.command.runtime.rule.InsertObjectCommand command = (org.drools.core.command.runtime.rule.InsertObjectCommand) commandsFactory.newInsert(object);
                    command.setReturnObject(insertObjectCommand.isReturnObject());
                    command.setEntryPoint(insertObjectCommand.getEntryPoiny());
                    if (insertObjectCommand.isSetOutIdentifier()) {
                        command.setOutIdentifier(insertObjectCommand.outIdentifier);
                    }
                    commands.add(command);
                }
            }
            if (batchExecutionCommand.isSetInsertElementsCommands()) {
                for (InsertElementsCommand insertElementsCommand : batchExecutionCommand.getInsertElementsCommands()) {
                    List<Object> objects = Converter.ConvertCommandPayLoad(insertElementsCommand, kieContainerClassLoader);
                    org.drools.core.command.runtime.rule.InsertElementsCommand command = (org.drools.core.command.runtime.rule.InsertElementsCommand) commandsFactory.newInsertElements(objects);
                    command.setReturnObject(insertElementsCommand.isReturnObject());
                    command.setEntryPoint(insertElementsCommand.getEntryPoiny());
                    if (insertElementsCommand.isSetOutIdentifier()) {
                        command.setOutIdentifier(insertElementsCommand.outIdentifier);
                    }
                    commands.add(command);
                }
            }
            if (batchExecutionCommand.isSetSetGlobalCommands()) {
                for (SetGlobalCommand setGlobalCommand : batchExecutionCommand.getSetGlobalCommands()) {
                    Object object = Converter.ConvertCommandPayLoad(setGlobalCommand, kieContainerClassLoader);
                    org.drools.core.command.runtime.SetGlobalCommand command = (org.drools.core.command.runtime.SetGlobalCommand) commandsFactory.newSetGlobal(setGlobalCommand.getIdentifier(), object);
                    if (setGlobalCommand.isSetOutIdentifier()) {
                        command.setOutIdentifier(setGlobalCommand.outIdentifier);
                    }
                    commands.add(command);
                }
            }
            FireAllRulesCommand fireAllRulesCommand = batchExecutionCommand.getFireAllRulesCommand();
            org.drools.core.command.runtime.rule.FireAllRulesCommand command = (org.drools.core.command.runtime.rule.FireAllRulesCommand) commandsFactory.newFireAllRules();
            command.setMax(fireAllRulesCommand.getMax());
            if (fireAllRulesCommand.isSetOutIdentifier()) {
                command.setOutIdentifier(fireAllRulesCommand.getOutIdentifier());
            }
            if (fireAllRulesCommand.isSetAgendaFilter()) {
                AgendaFilter agendaFilter = fireAllRulesCommand.getAgendaFilter();
                org.kie.api.runtime.rule.AgendaFilter filter = null;
                switch (agendaFilter.agendaFilterType) {
                    case RULE_NAME_STARTS_WITH:
                        filter = new RuleNameStartsWithAgendaFilter(agendaFilter.getExpression(), agendaFilter.isAccept());
                        break;
                    case RULE_NAME_ENDS_WITH:
                        filter = new RuleNameEndsWithAgendaFilter(agendaFilter.getExpression(), agendaFilter.isAccept());
                        break;
                    case RULE_NAME_EQUALS:
                        filter = new RuleNameEqualsAgendaFilter(agendaFilter.getExpression(), agendaFilter.isAccept());
                        break;
                    case RULE_NAME_MATCHES:
                        filter = new RuleNameMatchesAgendaFilter(agendaFilter.getExpression(), agendaFilter.isAccept());
                        break;
                    default:
                        throw new RuntimeException("Unknown AgendaFilter " + agendaFilter.agendaFilterType.name());
                }
                command.setAgendaFilter(filter);
            }
            commands.add(command);
            org.kie.api.runtime.ExecutionResults results = rulesExecutionService.call(kci, commandsFactory.newBatchExecution(commands, ksessionId));
            ExecutionResults executionResults = new ExecutionResults();
            for (String identifier : results.getIdentifiers()) {
                Object result = results.getValue(identifier);
                if (result instanceof ArrayList<?>) {
                    ArrayList<TBase> tBases = Converter.convertResultArray(result);
                    Collection collection = new Collection();
                    for(TBase tBase : tBases) {
                        collection.addToObjects(ByteBuffer.wrap(tSerializer.serialize(tBase)));
                    }
                    executionResults.putToObjects(identifier, ByteBuffer.wrap(tSerializer.serialize(collection)));
                    executionResults.addToOutIdentifiers(identifier);
                } else {
                    TBase tBase = Converter.convertResult(result);
                    executionResults.putToObjects(identifier, ByteBuffer.wrap(tSerializer.serialize(tBase)));
                    executionResults.addToOutIdentifiers(identifier);
                }
            }
            logger.debug("Returning OK response");
            return new KieServicesResponse().setResponse(Response.results(Results.executionResults(executionResults)));
        } catch (Exception e) {
            // in case marshalling failed return the call container response to keep backward compatibility
            String response = "Execution failed with error : " + e.getMessage();
            logger.warn("Returning Failure response with content '{}'", response);
            KieServicesResponse kieServicesResponse = new KieServicesResponse();
            kieServicesResponse.setResponse(Response.kieServicesException(new KieServicesException(e.getClass().getCanonicalName(), e.getMessage())));
            return kieServicesResponse;
        } finally {
            if (tDeserializer != null) {
                ThriftMessageReader.addDeserializer(tDeserializer);
            }
            if (tSerializer != null) {
                ThriftMessageWriter.addSerializer(tSerializer);
            }
        }
    }

}
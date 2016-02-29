package org.kie.server.remote.rest.thrift.drools;

import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.drools.RulesExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by x3.mbetzel on 04.01.2016.
 */
public class DroolsRestThriftApplicationComponentsService implements KieServerApplicationComponentsService {

    private static final String OWNER_EXTENSION = "Drools";

    private static final Logger logger = LoggerFactory.getLogger(DroolsRestThriftApplicationComponentsService.class);

    public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {
        // skip calls from other than owning extension
        if ( !OWNER_EXTENSION.equals(extension) ) {
            return Collections.emptyList();
        }

        RulesExecutionService rulesExecutionService = null;
        KieServerRegistry context = null;

        for( Object object : services ) {
            if( RulesExecutionService.class.isAssignableFrom(object.getClass()) ) {
                rulesExecutionService = (RulesExecutionService) object;
                continue;
            } else if( KieServerRegistry.class.isAssignableFrom(object.getClass()) ) {
                context = (KieServerRegistry) object;
                continue;
            }
        }

        List<Object> components = new ArrayList(1);
        if( SupportedTransports.REST.equals(type) ) {
            components.add(new CommandResource(rulesExecutionService, context));
        }

        logger.info("Drools KIE Server Thrift extension has been successfully registered as server extension");
        return components;
    }

}
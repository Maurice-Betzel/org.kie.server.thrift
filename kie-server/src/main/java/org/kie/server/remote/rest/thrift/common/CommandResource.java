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
package org.kie.server.remote.rest.thrift.common;

import org.drools.compiler.kie.builder.impl.InternalKieScanner;
import org.kie.server.api.KieServerConstants;
import org.kie.server.api.KieServerEnvironment;
import org.kie.server.services.api.KieContainerCommandService;
import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.impl.KieContainerInstanceImpl;
import org.kie.server.services.impl.KieServerImpl;
import org.kie.server.services.impl.KieServerLocator;
import org.kie.server.thrift.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

@Path("server/thrift")
public class CommandResource {

    private KieServerImpl kieServer;
    private KieContainerCommandService kieContainerCommandService;

    private static final Logger logger = LoggerFactory.getLogger(CommandResource.class);


    public CommandResource() {
        // if no server impl is passed as parameter, create one
        this.kieServer = KieServerLocator.getInstance();
    }

    public CommandResource(KieContainerCommandService kieContainerCommandService) {
        this.kieServer = KieServerLocator.getInstance();
        this.kieContainerCommandService = kieContainerCommandService;
    }

    @GET
    public KieServicesResponse getInfo() {
        KieServicesResponse kieServicesResponse = new KieServicesResponse();
        ServerInfo serverInfo = new ServerInfo();
        Version version = new Version();
        version.setMajor(KieServerEnvironment.getVersion().getMajor());
        version.setMinor(KieServerEnvironment.getVersion().getMinor());
        version.setRevision(KieServerEnvironment.getVersion().getRevision());
        version.setClassifier(KieServerEnvironment.getVersion().getClassifier());
        serverInfo.setVersion(version);
        serverInfo.setServerId(KieServerEnvironment.getServerId());
        serverInfo.setServerName(KieServerEnvironment.getServerName());
        for (KieServerExtension extension : kieServer.getServerRegistry().getServerExtensions()) {
            serverInfo.addToServerExtensions(extension.getImplementedCapability());
        }
        serverInfo.setKieServerLocation(System.getProperty(KieServerConstants.KIE_SERVER_LOCATION));
        kieServicesResponse.setResponse(Response.info(Info.serverInfo(serverInfo)));
        return kieServicesResponse;
    }

    @GET
    @Path("containers")
    public KieServicesResponse listContainers(@Context HttpHeaders httpHeaders) {
        KieServicesResponse kieServicesResponse = new KieServicesResponse();
        ListContainers listContainers = new ListContainers();
        List<KieContainerInstanceImpl> kieContainerInstances = kieServer.getServerRegistry().getContainers();
        for(KieContainerInstance kieContainerInstance : kieContainerInstances) {
            listContainers.addToContainers(kieContainerInstance.getContainerId());

        }
        kieServicesResponse.setResponse(Response.info(Info.listContainers(listContainers)));
        return kieServicesResponse;
    }

    @GET
    @Path("containers/{id}")
    public KieServicesResponse getContainerInfo(@PathParam("id") String id) {
        KieServicesResponse kieServicesResponse = new KieServicesResponse();
        KieContainerInstanceImpl kieContainerInstance = kieServer.getServerRegistry().getContainer(id);
        if( kieContainerInstance == null ) {
            logger.warn("No container deployed with id '{}'", id);
            return kieServicesResponse.setResponse(Response.kieServicesException(new KieServicesException("NoContainerException", id + " container not deployed")));
        } else {
            ContainerInfo containerInfo = new ContainerInfo();
            containerInfo.setContainerId(kieContainerInstance.getContainerId());
            containerInfo.setKieContainerStatus(KieContainerStatus.valueOf(kieContainerInstance.getStatus().name()));
            ReleaseId releaseId = new ReleaseId();
            releaseId.setArtifactId(kieContainerInstance.getKieContainer().getReleaseId().getArtifactId());
            releaseId.setGroupId(kieContainerInstance.getKieContainer().getReleaseId().getGroupId());
            releaseId.setVersion(kieContainerInstance.getKieContainer().getReleaseId().getVersion());
            containerInfo.setReleaseId(releaseId);
            KieScannerInfo kieScannerInfo = new KieScannerInfo();
            InternalKieScanner internalKieScanner = kieContainerInstance.getScanner();
            if (internalKieScanner == null) {
                kieScannerInfo.setKieScannerStatus(KieScannerStatus.UNKNOWN);
                kieScannerInfo.setPollInterval(-1);
            } else {
                kieScannerInfo.setKieScannerStatus(KieScannerStatus.valueOf(kieContainerInstance.getScanner().getStatus().name()));
                kieScannerInfo.setPollInterval(kieContainerInstance.getScanner().getPollingInterval());
            }
            containerInfo.setKieScannerInfo(kieScannerInfo);
            kieServicesResponse.setResponse(Response.info(Info.ContainerInfo(containerInfo)));
        }
        return kieServicesResponse;
    }

}
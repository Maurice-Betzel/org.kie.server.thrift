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

import org.kie.server.services.api.KieContainerCommandService;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.SupportedTransports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KieServerRestThriftApplicationComponentsService implements KieServerApplicationComponentsService {

    private static final String OWNER_EXTENSION = "KieServer";

    private static final Logger logger = LoggerFactory.getLogger(KieServerRestThriftApplicationComponentsService.class);

    @Override
    public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {
        // skip calls from other than owning extension
        if (!OWNER_EXTENSION.equals(extension)) {
            return Collections.emptyList();
        }

        KieContainerCommandService kieContainerCommandService = null;

        for (Object object : services) {
            if (KieContainerCommandService.class.isAssignableFrom(object.getClass())) {
                kieContainerCommandService = (KieContainerCommandService) object;
                break;
            }
        }

        List<Object> components = new ArrayList(1);
        if (SupportedTransports.REST.equals(type)) {
            components.add(new CommandResource(kieContainerCommandService));
        }

        logger.info("KIE Server Thrift extension has been successfully registered as server extension");
        return components;
    }

}
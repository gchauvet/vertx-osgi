/*
 * Copyright 2016 Guillaume Chauvet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zatarox.osgi.vertx.core;

import io.vertx.core.Vertx;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.apache.felix.ipojo.annotations.*;
import java.util.HashSet;
import java.util.Set;
import static io.zatarox.osgi.vertx.core.impl.TcclSwitch.executeWithTCCLSwitch;

/**
 * A component gistering the Vert.x instance as an OSGi service.
 */
@Component
@Provides(strategy = "SERVICES" )
public class VertxComponent {

    @Context
    private BundleContext context;
    private final Set<ServiceRegistration> registrations = new HashSet<>();

    @Validate
    public void start() throws Exception {
        final Vertx vertx = executeWithTCCLSwitch(() -> Vertx.vertx());
        registrations.add(context.registerService(Vertx.class, vertx, null));
    }

    @Invalidate
    public void stop() throws Exception {
        registrations.stream().forEach(ServiceRegistration::unregister);
    }
}

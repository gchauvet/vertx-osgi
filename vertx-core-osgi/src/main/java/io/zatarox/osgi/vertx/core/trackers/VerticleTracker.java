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
package io.zatarox.osgi.vertx.core.trackers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import java.util.HashSet;
import java.util.Set;
import org.apache.felix.ipojo.annotations.*;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@Component(immediate = true)
@Provides
@Whiteboards(whiteboards = {
    @Wbp(
        filter = "(objectClass=io.vertx.core.AbstractVerticle)",
        onArrival = "onArrival",
        onDeparture = "onDeparture"
    )
})
public final class VerticleTracker {

    @Requires
    private Vertx vertx;
    @Context
    private BundleContext context;

    private final Set<ServiceReference<AbstractVerticle>> services = new HashSet<>();

    public void onArrival(ServiceReference<AbstractVerticle> ref) {
        vertx.deployVerticle(context.getService(ref));
        services.add(ref);
    }

    public void onDeparture(ServiceReference<AbstractVerticle> ref) {
        final AbstractVerticle instance = context.getService(ref);
        instance.getVertx().undeploy(instance.deploymentID(), event -> {
            services.remove(ref);
        });
    }

}

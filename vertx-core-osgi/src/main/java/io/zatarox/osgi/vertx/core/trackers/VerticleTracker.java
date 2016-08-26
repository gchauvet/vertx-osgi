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

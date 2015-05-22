package simplest

import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.routing.Router

@CompileStatic
class SimplestRouter extends Router {

    @Inject
    Context context

    @Override
    void start() throws Exception {
        logger.info("Starting ${this.class}")
        attach("/foos/", FoosResource)
        super.start()
    }

    @Override
    void stop() throws Exception {
        logger.info("Stopping ${this.class}")
        super.stop()
        getRoutes().clear()
    }
}

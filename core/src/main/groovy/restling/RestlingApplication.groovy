package restling

import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.Application
import org.restlet.Context
import org.restlet.Restlet
import org.restlet.routing.Router
/**
 * The container of all the Restling magic.
 */
@CompileStatic
class RestlingApplication extends Application {

    Provider<Router> inboundRootProvider

    RestlingApplication(Context context) {
        super(context)
    }

    @Override
    Restlet createInboundRoot() {
        Objects.requireNonNull(inboundRootProvider, "please provide an inboundRootProvider")
        Router router = inboundRootProvider.get()
        router.context = context
        return router
    }


}

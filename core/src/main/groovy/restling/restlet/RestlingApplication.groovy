package restling.restlet

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.Application
import org.restlet.Context
import org.restlet.Restlet

/**
 * The container of all the Restling magic.
 */
@CompileStatic
class RestlingApplication extends Application {

    @Inject
    Provider<RestlingRouter> inboundRootProvider

    @Inject
    RestlingApplication(Context context) {
        super(context)
    }

    @Override
    Restlet createInboundRoot() {
        assert inboundRootProvider: "please provide an inboundRootProvider"
        RestlingRouter router = inboundRootProvider.get()
        assert router: "the inbound root provider returned null"
        router.init()
        return router
    }


}

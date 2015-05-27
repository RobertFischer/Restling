package restling.restlet

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.Application
import org.restlet.Context

/**
 * The container of all the Restling magic.
 */
@CompileStatic
class RestlingApplication extends Application {

    /**
     * The {@link Provider} used to {@link #createInboundRoot()}.
     */
    @Inject
    Provider<RestlingRouter> inboundRootProvider

    /**
     * Constructor (passes through to the superclass).
     *
     * {@inheritDoc}
     */
    @Inject
    RestlingApplication(Context context) {
        super(context)
        tunnelService.extensionsTunnel = true
    }

    @Override
    RestlingRouter createInboundRoot() {
        assert inboundRootProvider: "please provide an inboundRootProvider"
        RestlingRouter router = inboundRootProvider.get()
        assert router: "the inbound root provider returned null"
        router.init()
        return router
    }

}

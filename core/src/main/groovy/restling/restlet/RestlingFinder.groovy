package restling.restlet

import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.resource.Finder
import org.restlet.resource.ServerResource
import restling.guice.modules.RequestModule
import restling.guice.modules.RequestResponseModule

/**
 * Uses a Guice {@link Injector} to instantiate the target class on demand.
 */
@CompileStatic
class RestlingFinder extends Finder {

    /**
     * The parent injector for all instantiations
     */
    final Injector injector

    RestlingFinder(Context context, Class<? extends ServerResource> targetClass, Injector injector) {
        super(context, targetClass)
        assert injector: "Need an Injector for a RestlingFinder"
        this.injector = injector
    }

    /**
     * Creates a child injector that exposes the arguments, and then calls the child injector to instantiate the target class.
     *
     * @param request The request that will be put into the injector context
     * @param response The response that will be put into the injector context
     * @return The instantiated resource
     */
    @Override
    ServerResource find(Request request, Response response) {
        def requestResponseModule = injector.getInstance(RequestResponseModule)
        requestResponseModule.request = request
        requestResponseModule.response = response

        Injector requestInjector = injector.createChildInjector(requestResponseModule)
        requestInjector = requestInjector.createChildInjector(requestInjector.getInstance(RequestModule))
        return requestInjector.getInstance(targetClass)
    }

}

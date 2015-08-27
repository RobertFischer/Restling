package restling.restlet

import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.resource.Finder
import org.restlet.resource.ServerResource
import restling.guice.RequestInjectorFactory

/**
 * Uses a Guice {@link Injector} to instantiate the target class on demand.
 */
@CompileStatic
class RestlingFinder extends Finder {

    /**
     * The parent injector for all instantiations
     */
    final RequestInjectorFactory injectorFactory

    RestlingFinder(Context context, Class<? extends ServerResource> targetClass, RequestInjectorFactory injectorFactory) {
        super(context, targetClass)
        assert injectorFactory: "Need an InjectorFactory for a RestlingFinder"
        this.injectorFactory = injectorFactory
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
        Injector requestInjector = injectorFactory.create(request, response)
        return requestInjector.getInstance(targetClass)
    }

}

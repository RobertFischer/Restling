package restling.restlet

import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.Restlet
import org.restlet.routing.Router
import restling.guice.RequestInjectorFactory

/**
 * Given a {@link Router} class, it instantiates the router per request.
 */
@CompileStatic
class RestlingInjectedRouter extends Restlet {

    final Class<? extends Router> targetClass
    final RequestInjectorFactory injectorFactory

    RestlingInjectedRouter(Context context, Class<? extends Router> targetClass, RequestInjectorFactory injectorFactory) {
        super(context)
        assert targetClass: "Need a target class to build"
        this.targetClass = targetClass

        assert injectorFactory: "Need an InjectorFactory to build $targetClass"
        this.injectorFactory = injectorFactory
    }

    @Override
    void handle(Request request, Response response) {
        super.handle(request, response)
        def target = injectorFactory.create(request, response).getInstance(targetClass)
        if (target instanceof RestlingRouter) {
            def r = target as RestlingRouter
            r.context = context
            r.init()
        }
        target.handle(request, response)
    }
}

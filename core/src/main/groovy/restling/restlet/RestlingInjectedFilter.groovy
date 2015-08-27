package restling.restlet

import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.Restlet
import org.restlet.routing.Filter
import restling.guice.RequestInjectorFactory

/**
 * Given a {@link Filter} class, it instantiates the filter per request, optionally assigning
 * {@link Filter#setNext(org.restlet.Restlet)} using the {@code next} property.
 */
@CompileStatic
class RestlingInjectedFilter extends Restlet {

    Restlet next
    final Class<? extends Filter> targetClass
    final RequestInjectorFactory injectorFactory

    RestlingInjectedFilter(Context context, Class<? extends Filter> targetClass, RequestInjectorFactory injectorFactory) {
        super(context)
        assert targetClass: "Need a target class to build"
        this.targetClass = targetClass

        assert injectorFactory: "Need an InjectorFactory to build $targetClass"
        this.injectorFactory = injectorFactory
    }

    @Override
    void handle(Request request, Response response) {
        super.handle(request, response)
        Filter filter = injectorFactory.create(request, response).getInstance(targetClass)
        if (next) filter.next = next
        filter.handle(request, response)
    }
}

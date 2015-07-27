package restling.guice.modules

import com.google.inject.Binder
import com.google.inject.Module
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.restlet.Request
import org.restlet.Response

/**
 * A module that provides {@link Request} and {@link Response} implementations.
 */
@TupleConstructor
@CompileStatic
class RequestResponseModule implements Module {

    Request request
    Response response

    /**
     * Contributes bindings and other configurations for this module to {@code binder}.
     *
     * <p><strong>Do not invoke this method directly</strong> to install submodules. Instead use
     * {@link Binder#install(Module)}, which ensures that {@link Provides provider methods} are
     * discovered.
     */
    @Override
    void configure(Binder binder) {
        binder.with {
            bind(Request).toInstance(request)
            bind(Response).toInstance(response)
        }
    }
}

package restling.guice.modules

import com.google.inject.AbstractModule
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
class RequestResponseModule extends AbstractModule {

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
    void configure() {
        bind(Request).toInstance(request)
        bind(Response).toInstance(response)
    }
}

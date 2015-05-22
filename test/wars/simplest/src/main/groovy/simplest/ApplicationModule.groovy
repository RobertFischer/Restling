package simplest

import com.google.inject.Binder
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.routing.Router

@CompileStatic
class ApplicationModule implements Module {
    /**
     * Contributes bindings and other configurations for this module to {@code binder}.
     *
     * <p><strong>Do not invoke this method directly</strong> to install submodules. Instead use
     * {@link Binder#install(Module)}, which ensures that {@link Provides provider methods} are
     * discovered.
     */
    @Override
    void configure(Binder binder) {
        binder.bind(Router).to(SimplestRouter)
    }

}

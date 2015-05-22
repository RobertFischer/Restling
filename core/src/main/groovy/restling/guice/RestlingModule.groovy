package restling.guice

import com.google.inject.Binder
import com.google.inject.Module
import groovy.transform.CompileStatic
import org.restlet.Application
import org.restlet.Context
import restling.RestlingApplication

/**
 * The Guice module responsible for wiring together the Restling infrastructure.
 */
@CompileStatic
class RestlingModule implements Module {

    RestlingApplication application

    RestlingModule(RestlingApplication application) {
        this.application = application
    }

    /**
     * Contributes bindings and other configurations for this module to {@code binder}.
     *
     * <p><strong>Do not invoke this method directly</strong> to install submodules. Instead use
     * {@link Binder#install(Module)}, which ensures that provider methods are
     * discovered.
     */

    @Override
    void configure(Binder binder) {
        assert application: "Please provide an application before executing the binding"
        binder.bind(RestlingApplication).toInstance(application)
        binder.bind(Application).toInstance(application)
        binder.bind(Context).toInstance(application.context)
    }

}

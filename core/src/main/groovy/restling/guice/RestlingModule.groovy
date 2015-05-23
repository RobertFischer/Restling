package restling.guice

import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import org.restlet.Context

/**
 * The Guice module responsible for wiring together the Restling infrastructure.
 */
@CompileStatic
class RestlingModule implements Module {

    final Context context
    final Class<? extends RestlingApplicationModule> applicationModule

    RestlingModule(Context context, Class<? extends RestlingApplicationModule> applicationModule) {
        assert context: "Please provide a context for ${this.class}"
        this.context = context
        assert applicationModule: "Please specify an application module class for ${this.class}"
        this.applicationModule = applicationModule
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
        binder.with {
            bind(Context).toInstance(context)
            bind(RestlingApplicationModule).to(applicationModule).in(Singleton)
        }
    }

}

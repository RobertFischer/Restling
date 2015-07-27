package restling.guice.modules

import com.google.inject.AbstractModule
import com.google.inject.Binder
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import restling.restlet.RestlingApplication
import restling.restlet.RestlingRouter

/**
 * The base class that you should use for defining your application's Guice dependencies.
 */
@CompileStatic
abstract class RestlingApplicationModule extends AbstractModule {

    /**
     * Performs the configuration for the Restling application. Subclasses are expected to
     * provide an implementation to {@link #configureCustomBindings()}, which is
     * called from this method. This method will also provide the various necessary bits
     * of infrastructure for the RestlingApplication.
     *
     * @param binder The binding object; never {@code null}
     */
    @Override
    final void configure() {
        bind(RestlingRouter).to(routerClass).in(Singleton)

        // The fact that bind(RestlingApplication).to(RestlingApplication)
        // is an error condition in Guice is stupid...but it is what it is.
        def applicationBuilder = bind(RestlingApplication)
        if (applicationClass != RestlingApplication) {
            applicationBuilder = applicationBuilder.to(applicationClass)
        }
        applicationBuilder.in(Singleton)

        if (requestModuleClass != RequestModule) {
            bind(RequestModule).to(requestModuleClass)
        }

        configureCustomBindings()
    }

    /**
     * This is called at the end of {@link #configure(Binder)}, and is where you should provide any
     * application-specific bindings that you need.
     */
    abstract void configureCustomBindings();

    /**
     * Hook to provide the {@link RestlingApplication} implementation to use as your application.
     * By default, it returns {@link RestlingApplication} itself, but you are welcome to subclass it.
     *
     * @return The application class to use
     */
    Class<? extends RestlingApplication> getApplicationClass() {
        return RestlingApplication
    }

    /**
     * Hook to provide the {@link RestlingRouter} implementation to use for your application.
     * This class will be used for handling incoming requests to your Restling application.
     *
     * @return The class that will be used for routing
     */
    abstract Class<? extends RestlingRouter> getRouterClass();

    /**
     * Hook to provide a {@link com.google.inject.Module} that is loaded on a per-request basis, beyond
     * the {@link RequestResponseModule}.
     *
     * @return The module loaded per request; by default, {@link RequestModule}
     */
    Class<? extends RequestModule> getRequestModuleClass() {
        return RequestModule
    }

}

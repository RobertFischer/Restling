package restling.guice

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.junit.Test
import org.restlet.Context
import org.restlet.ext.jackson.JacksonConverter
import restling.guice.modules.RestlingApplicationModule
import restling.restlet.RestlingJacksonConverter
import restling.restlet.RestlingRouter

@CompileStatic
class RestlingApplicationModuleTest {

    static class DoNothingRestlingRouter extends RestlingRouter {

        /**
         * This is the hook for performing initialization (such as calling {@code .attach})
         * for this router.
         */
        @Inject
        DoNothingRestlingRouter(Context context) {
            super(context)
        }

        @Override
        void init() throws Exception {

        }
    }

    static class BasicRestlingApplicationModule extends RestlingApplicationModule {

        boolean customBindingsConfigured = false

        /**
         * This is called at the end of {@link #configure(Binder)}, and is where you should provide any
         * application-specific bindings that you need.
         *
         * @param binder The binding object; never {@code null}
         */
        @Override
        void configureCustomBindings(Binder binder) {
            assert !customBindingsConfigured: "Custom bindings already configured"
            customBindingsConfigured = true
        }

        /**
         * Hook to provide the {@link RestlingRouter} implementation to use for your application.
         * This class will be used for handling incoming requests to your Restling application.
         *
         * @return The class that will be used for routing
         */
        @Override
        Class<? extends RestlingRouter> getRouterClass() {
            return DoNothingRestlingRouter
        }
    }

    @Test
    void constructJacksonConverter() {
        def fixture = new BasicRestlingApplicationModule()
        def jax = Guice.createInjector(fixture).getInstance(JacksonConverter)
        assert jax instanceof RestlingJacksonConverter
    }

}

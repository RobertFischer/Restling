package restling.guice

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.junit.Test
import org.restlet.Context
import restling.guice.modules.RestlingApplicationModule
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
        void configureCustomBindings() {
            assert !customBindingsConfigured: "Custom bindings already configured"
            customBindingsConfigured = true
        }

        @Override
        Class<? extends RestlingRouter> getRouterClass() {
            return DoNothingRestlingRouter
        }
    }

    @Test
    void smoketest() {
        Guice.createInjector(new BasicRestlingApplicationModule()).getInstance(Injector)
    }


}

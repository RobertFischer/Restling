package simplest

import com.google.inject.Binder
import groovy.transform.CompileStatic
import restling.guice.modules.RestlingApplicationModule

@CompileStatic
class ApplicationModule extends RestlingApplicationModule {

    Class<SimplestRouter> routerClass = SimplestRouter

    /**
     * Does nothing.
     */
    @Override
    void configureCustomBindings() {}

}

package application

import com.google.inject.Binder
import groovy.transform.CompileStatic
import restling.guice.modules.RestlingApplicationModule

@CompileStatic

class ApplicationModule extends RestlingApplicationModule {

    Class<ApplicationRouter> routerClass = ApplicationRouter

    @Override
    void configureCustomBindings(Binder binder) {
    }
}

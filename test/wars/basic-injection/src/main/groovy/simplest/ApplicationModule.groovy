package simplest

import com.google.inject.Binder
import com.google.inject.name.Names
import groovy.transform.CompileStatic
import restling.guice.RestlingApplicationModule

@CompileStatic
class ApplicationModule extends RestlingApplicationModule {

    Class<ApplicationRouter> routerClass = ApplicationRouter

    @Override
    void configureCustomBindings(Binder binder) {
        binder.bindConstant().annotatedWith(Names.named("message")).to("Hello, World!")
    }
}

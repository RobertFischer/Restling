package attachFilters

import com.google.inject.name.Names
import groovy.transform.CompileStatic
import restling.guice.modules.RestlingApplicationModule

@CompileStatic
class ApplicationModule extends RestlingApplicationModule {

    Class<ApplicationRouter> routerClass = ApplicationRouter

    @Override
    void configureCustomBindings() {
        bindConstant().annotatedWith(Names.named("message")).to("Hello, World!")
    }
}

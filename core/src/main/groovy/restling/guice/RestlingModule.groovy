package restling.guice

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import org.restlet.Context
import restling.guice.providers.DefaultedProvider
import restling.guice.providers.ObjectMapperProvider

import javax.inject.Provider

/**
 * The Guice module responsible for wiring together the Restling infrastructure.
 */
@CompileStatic
class RestlingModule extends AbstractModule {

    final Context context
    final Class<? extends RestlingApplicationModule> applicationModule

    RestlingModule(Context context, Class<? extends RestlingApplicationModule> applicationModule) {
        assert context: "Please provide a context for ${this.class}"
        this.context = context
        assert applicationModule: "Please specify an application module class for ${this.class}"
        this.applicationModule = applicationModule
    }

    @Override
    void configure() {
        bind(Context).toInstance(context)
        bind(RestlingApplicationModule).to(applicationModule).in(Singleton)
        configureDefaultedBinding(ObjectMapper, ObjectMapperProvider)
    }

    public <T> void configureDefaultedBinding(Class<T> target, Class<Provider<T>> defaultProviderClass) {
        bind(Key.get(target, RestlingDefault)).toProvider(defaultProviderClass)
        bind(target).toProvider(new DefaultedProvider(target, getProvider(Injector)))

    }

}

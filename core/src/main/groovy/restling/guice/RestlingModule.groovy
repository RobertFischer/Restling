package restling.guice

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.ext.jackson.JacksonConverter
import restling.guice.providers.JacksonConverterProvider
import restling.guice.providers.ObjectMapperProvider

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
        bind(ObjectMapper).toProvider(ObjectMapperProvider)
        bind(JacksonConverter).toProvider(JacksonConverterProvider)
    }

}

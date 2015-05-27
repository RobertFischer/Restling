package restling.guice.providers

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Key
import com.google.inject.Provider
import com.google.inject.spi.Dependency
import com.google.inject.spi.ProviderWithDependencies
import groovy.transform.CompileStatic
import org.restlet.ext.jackson.JacksonConverter
import restling.restlet.RestlingJacksonConverter

/**
 * Provides the {@link RestlingJacksonConverter} in place of a {@link JacksonConverter}
 */
@CompileStatic
class JacksonConverterProvider implements Provider<RestlingJacksonConverter>, ProviderWithDependencies<RestlingJacksonConverter> {

    final Provider<ObjectMapper> omProvider

    @Inject
    JacksonConverterProvider(Provider<ObjectMapper> objectMapperProvider) {
        assert objectMapperProvider: "Need a provider for an ObjectMapper"
        this.omProvider = objectMapperProvider
    }

    @Override
    RestlingJacksonConverter get() {
        assert omProvider: "Need a provider for ObjectMapper!"
        return new RestlingJacksonConverter(omProvider)
    }

    @Override
    Set<Dependency<?>> getDependencies() {
        return ([ObjectMapper].collect { Dependency.get(Key.get(it)) }) as Set
    }

}

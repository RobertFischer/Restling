package restling.restlet

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.data.MediaType
import org.restlet.ext.jackson.JacksonConverter
import org.restlet.ext.jackson.JacksonRepresentation
import org.restlet.representation.Representation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Hijacks {@link JacksonConverter} to use the {@link ObjectMapper} from the Guice context.
 */
@CompileStatic
class RestlingJacksonConverter extends JacksonConverter {

    private static final Logger logger = LoggerFactory.getLogger(RestlingJacksonConverter)

    final Provider<ObjectMapper> objectMapperProvider

    @Inject
    RestlingJacksonConverter(Provider<ObjectMapper> omProvider) {
        assert omProvider: "No object mapper provider given!"
        this.objectMapperProvider = omProvider
    }

    @Override
    protected <T> JacksonRepresentation<T> create(MediaType mediaType, T source) {
        def result = super.create(mediaType, source)

        assert logger: "No logger provided"
        logger.info("Using custom object mapper!")
        assert objectMapperProvider: "No object mapper provider given!"
        result.objectMapper = objectMapperProvider.get()

        return result
    }

    @Override
    protected <T> JacksonRepresentation<T> create(Representation source, Class<T> objectClass) {
        def result = super.create(source, objectClass)

        assert logger: "No logger provided"
        logger.info("Using custom object mapper!")
        assert objectMapperProvider: "No object mapper provider given!"
        result.objectMapper = objectMapperProvider.get()

        return result
    }

}

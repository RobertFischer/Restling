package restling.guice.providers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Provider
import groovy.transform.CompileStatic

/**
 * Our provider for an {@link ObjectMapper}, which involves configuring a lot of sane defaults.
 */
@CompileStatic
class ObjectMapperProvider implements Provider<ObjectMapper> {

    @Override
    ObjectMapper get() {
        def om = new ObjectMapper()

        // Safety checks
        om.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        om.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
        om.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)

        // Convenience for sloppy clients
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        om.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

        //

        return om
    }
}

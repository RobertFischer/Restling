package restling.guice.providers

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
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

        // Enable JDK8 support
        def jdk8 = new Jdk8Module()
        jdk8.configureAbsentsAsNulls(true)
        om.registerModule(jdk8) // https://github.com/FasterXML/jackson-datatype-jdk8
        om.registerModule(new JavaTimeModule()) // https://github.com/FasterXML/jackson-datatype-jsr310
        om.registerModule(new ParameterNamesModule()); // https://github.com/FasterXML/jackson-module-parameter-names

        // Enable Third-Party library support
        om.registerModule(new JodaModule())
        def guava = new GuavaModule()
        guava.configureAbsentsAsNulls(true)
        om.registerModule(guava)

        // Safety checks
        om.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        om.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
        om.disable(MapperFeature.USE_GETTERS_AS_SETTERS)
        om.enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID)

        // Don't allow duplicate keys from clients
        om.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
        om.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION)
        om.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)

        // Things we do accept from sloppy clients
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        om.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
        om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        om.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
        om.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        om.enable(JsonParser.Feature.ALLOW_COMMENTS)
        om.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS)
        om.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        om.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
        om.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
        om.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)

        // We don't use nanoseconds
        om.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
        om.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)

        // Handle timezones in a sane way
        om.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
        om.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);

        // Return properties alphabetically for readers' convenience
        om.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        om.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)

        // Pretty-print the output: you don't have enough traffic to make this a problem, and it is very helpful for
        // those trying to read your resulting JSON.
        om.enable(SerializationFeature.INDENT_OUTPUT)

        // Enable the ability to "close" root entities that are {@link Closeable}
        om.enable(SerializationFeature.CLOSE_CLOSEABLE)

        // Don't write out map entries whose values are "null"
        om.disable(SerializationFeature.WRITE_NULL_MAP_VALUES)

        // Escape non-ASCII characters to avoid character encoding issues
        om.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII)

        // Don't intern all the field names: doesn't allow memory to be recovered
        om.factory.disable(JsonFactory.Feature.INTERN_FIELD_NAMES);

        return om
    }
}

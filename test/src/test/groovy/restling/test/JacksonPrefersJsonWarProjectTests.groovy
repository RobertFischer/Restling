package restling.test

import groovy.transform.CompileStatic
import org.junit.Test
import restling.guice.providers.ObjectMapperProvider

@CompileStatic
class JacksonPrefersJsonWarProjectTests extends WarProjectTests {

    JacksonPrefersJsonWarProjectTests() {
        super("jackson-prefer-json")
    }

    @Test
    void testTruth() {
        assert true
    }

    @Test
    void retrieveResource() {
        URL url = new URL("http://localhost:$server.port/foos")
        assert url
        assert url.protocol == "http"
        assert url.port == server.port
        String text = url.text.trim()

        assert text.startsWith("{")
        assert text.endsWith("}")

        def result = new ObjectMapperProvider().get().reader(Map).readValue(text)
        assert [hello: true] == result
    }

    @Test
    void retrieveResourceJson() {
        URL url = new URL("http://localhost:$server.port/foos.json")
        assert url
        assert url.protocol == "http"
        assert url.port == server.port
        String text = url.text.trim()

        assert text.startsWith("{")
        assert text.endsWith("}")

        def result = new ObjectMapperProvider().get().reader(Map).readValue(text)
        assert [hello: true] == result
    }

    @Test
    void retrieveResourceXml() {
        URL url = new URL("http://localhost:$server.port/foos.xml")
        assert url
        assert url.protocol == "http"
        assert url.port == server.port
        String text = url.text.trim()
        assert text == "<LinkedHashMap><hello>true</hello></LinkedHashMap>"
    }


}

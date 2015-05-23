package restling.test

import groovy.transform.CompileStatic
import org.junit.Test

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
        assert text == "{\"hello\":true}"
    }


}

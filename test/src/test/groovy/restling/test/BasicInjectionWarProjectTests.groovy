package restling.test

import groovy.transform.CompileStatic
import org.junit.Test

@CompileStatic
class BasicInjectionWarProjectTests extends WarProjectTests {

    BasicInjectionWarProjectTests() {
        super("basic-injection")
    }

    @Test
    void testTruth() {
        assert true
    }

    @Test
    void retrieveHelloFile() {
        URL url = new URL("http://localhost:$server.port/foos/")
        assert url
        assert url.protocol == "http"
        assert url.port == server.port
        String text = url.text.trim()
        assert text == "Hello, World!"
    }

}

package restling.restlet

import org.restlet.Context
import spock.lang.Specification

/**
 * Specification to check that {@link Context} works.
 */
class ContextSpec extends Specification {

    def "fresh context can be asserted"() {
        expect:
        assert new Context(): "No context present!"
    }

    def "fresh context responds to asBoolean"() {
        expect:
        new Context().asBoolean()
    }


}

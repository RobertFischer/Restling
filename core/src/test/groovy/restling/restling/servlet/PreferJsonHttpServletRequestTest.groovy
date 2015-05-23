package restling.restling.servlet

import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest

@CompileStatic
class PreferJsonHttpServletRequestTest extends GroovyTestCase {

    HttpServletRequest mockServletWithFileExtension() {
        [getPathInfo   : { -> "foo.xml" },
         getHeaders    : { String header -> Collections.enumeration([header]) },
         getHeader     : { String header -> header },
         getHeaderNames: { -> Collections.enumeration(["foobar"]) }
        ] as HttpServletRequest
    }

    HttpServletRequest mockServletWithoutFileExtension() {
        [getPathInfo   : { -> "foo" },
         getHeaders    : { String header -> Collections.enumeration([header]) },
         getHeader     : { String header -> header },
         getHeaderNames: { -> Collections.enumeration([]) }
        ] as HttpServletRequest
    }

    void testGetHeadersWithJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithoutFileExtension())
        Enumeration<String> result = fixture.getHeaders("Accept")
        assert result?.nextElement() == "application/json"
        assert !result.hasMoreElements()

        result = fixture.getHeaders("foobar")
        assert result?.nextElement() == "foobar"
        assert !result.hasMoreElements()
    }

    void testGetHeadersWithoutJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithFileExtension())
        Enumeration<String> result = fixture.getHeaders("Accept")
        assert result?.nextElement() == "Accept"
        assert !result.hasMoreElements()

        result = fixture.getHeaders("foobar")
        assert result?.nextElement() == "foobar"
        assert !result.hasMoreElements()
    }

    void testGetHeaderWithJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithoutFileExtension())
        def result = fixture.getHeader("Accept")
        assert result == "application/json"

        result = fixture.getHeader("foobar")
        assert result == "foobar"
    }

    void testGetHeaderWithoutJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithFileExtension())

        def result = fixture.getHeader("Accept")
        assert result == "Accept"

        result = fixture.getHeader("foobar")
        assert result == "foobar"
    }

    void testGetHeaderNamesWithJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithoutFileExtension())
        assert fixture.getHeaderNames()?.nextElement() == "Accept"
    }

    void testGetHeaderNamesWithoutJsonApplied() {
        def fixture = new PreferJsonHttpServletRequest(mockServletWithFileExtension())
        assert (Collections.list(fixture.getHeaderNames()) as Set) == (["Accept", "foobar"] as Set)
    }
}

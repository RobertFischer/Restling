package restling.restling.servlet

import com.google.common.io.Files
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * Wraps the {@link HttpServletRequest} to specify the JSON type if there was no file extension.
 */
@CompileStatic
class PreferJsonHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * Wraps the given request, if necessary. (If there is no need to wrap the request, returns the original request.)
     *
     * @param request
     * @return
     */
    static HttpServletRequest wrap(HttpServletRequest request) {
        def wrapped = new PreferJsonHttpServletRequest(request)
        if (wrapped.applyJson) return wrapped
        return request
    }

    final boolean applyJson

    /**
     * Constructs a request object wrapping the given request.
     * @throws java.lang.IllegalArgumentException if the request is null
     */
    PreferJsonHttpServletRequest(HttpServletRequest request) {
        super(request)

        // Figure out if we should apply the Json type
        def fileExt = Files.getFileExtension(request.pathInfo)
        applyJson = !(fileExt && !fileExt.isEmpty() && !fileExt.isAllWhitespace())
    }

    @Override
    Enumeration<String> getHeaders(String name) {
        if (applyJson && name.equalsIgnoreCase("Accept")) {
            return Collections.enumeration(["application/json"])
        } else {
            return super.getHeaders(name)
        }
    }

    @Override
    String getHeader(String name) {
        if (applyJson && name.equalsIgnoreCase("Accept")) {
            return "application/json"
        } else {
            return super.getHeader(name)
        }
    }

    @Override
    Enumeration<String> getHeaderNames() {
        def headerNames = new TreeSet<>(Collections.list(super.getHeaderNames()));
        headerNames.add("Accept")
        return Collections.enumeration(headerNames)
    }

}

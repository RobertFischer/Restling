package restling.restlet

import com.github.zafarkhaja.semver.Version
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.Restlet
import org.restlet.resource.Status

/**
 * Responsible for resolving the version information and assigning it into the {@code restling.api.version}
 * attribute of the {@link Request}.
 */
@Slf4j
@CompileStatic
class VersionRestlet extends Restlet {

    @Canonical
    private static class VersionData {
        Version header, url
    }

    @Status(412)
    private static class HeaderUrlVersionMismatchException extends Exception {
        final String error
        final VersionData versions

        HeaderUrlVersionMismatchException(VersionData versionData) {
            super("The URL version is greater than the header version: $versionData")
            this.error = message
            versions = versionData
        }
    }

    @Status(412)
    private static class NonsenseUrlVersionException extends Exception {
        final String error
        final String version

        NonsenseUrlVersionException(String version, Exception e) {
            super("The version given in the URL is nonsensical: $version", e)
            this.error = message
            this.version = version
        }
    }

    @Status(412)
    private static class NonsenseHeaderVersionException extends Exception {
        final String error
        final String version

        NonsenseHeaderVersionException(String version, Exception e) {
            super("The version given in the header is nonsensical: $version", e)
            this.error = message
            this.version = version
        }
    }


    static final String VERSION_HEADER_NAME = "API-VERSION"
    static final String VERSION_ATTRIBUTE_NAME = "restling.api.version"
    static final Version DEFAULT_VERSION = Version.forIntegers(0)

    final Restlet next

    /**
     * Constructor
     *
     * @param context The context to execute; may not be {@code null}
     * @param restlingRouter The {@link Restlet} to delegate to; may not be {@code null}
     */
    VersionRestlet(Context context, Restlet next) {
        super(context)
        assert next: "Cannot have a null next element"
        this.next = next
    }

    @Override
    void handle(Request request, Response response) {
        log.trace("In the handle method of ${this.class}")
        super.handle(request, response)

        log.trace("Searching out the version information")
        Version version = resolveVersion(request)
        request.attributes.put(VERSION_ATTRIBUTE_NAME, version)
        log.debug("Found version $version for request; inserted into the request's $VERSION_ATTRIBUTE_NAME attribute")

        this.next.handle(request, response)
    }

    /**
     * Resolves a version for the given request
     *
     * @param request The request to resolve; may not be {@code null}
     * @return A version to use, which may be {@link #DEFAULT_VERSION}
     */
    static Version resolveVersion(Request request) {
        assert request: "Request was null"
        def urlVersion = resolveUrlVersion(request)
        log.trace("Found $urlVersion for the url version of the request")
        def headerVersion = resolveHeaderVersion(request)
        log.trace("Found $headerVersion for the url version of the request")

        if (urlVersion && headerVersion && !urlVersion.lessThanOrEqualTo(headerVersion)) {
            throw new HeaderUrlVersionMismatchException(new VersionData(headerVersion, urlVersion))
        }
        return [DEFAULT_VERSION, urlVersion, headerVersion].findAll({ it != null }).max()
    }

    /**
     * Resolves the version based on {@link #VERSION_HEADER_NAME} being in the header.
     *
     * @param request The request to resolve; may not be {@code null}
     * @return The version based on the header, if any; may be {@code null} if none was found
     */
    static Version resolveHeaderVersion(Request request) {
        assert request: "Request was null"
        String rawVersion = request.headers.getFirstValue(VERSION_HEADER_NAME, true, null)
        if (!rawVersion) {
            log.debug("No header named $VERSION_HEADER_NAME was found")
            return null
        } else {
            log.trace("Raw version from $VERSION_HEADER_NAME header is: $rawVersion")
        }
        try {
            return Version.valueOf(rawVersion)
        } catch (Exception e) {
            throw new NonsenseHeaderVersionException(rawVersion, e)
        }
    }

    /**
     * Resolves the version based on {@link #VERSION_ATTRIBUTE_NAME} being in the URL attributes.
     *
     * @param request The request to resolve; may not be {@code null}
     * @return The version based on the URL, if any; may br {@code null} if none was found
     */
    static Version resolveUrlVersion(Request request) {
        assert request: "Request was null"
        String rawVersion = request.attributes.get(VERSION_ATTRIBUTE_NAME, null)
        if (!rawVersion) {
            log.debug("No URL attribute named $VERSION_ATTRIBUTE_NAME was found")
            return null
        } else {
            log.trace("Raw version from the $VERSION_ATTRIBUTE_NAME attribute of the URL is: $rawVersion")
        }
        try {
            return Version.valueOf(rawVersion)
        } catch (Exception e) {
            throw new NonsenseUrlVersionException(rawVersion, e)
        }
    }
}

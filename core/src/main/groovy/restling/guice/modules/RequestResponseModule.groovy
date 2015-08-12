package restling.guice.modules

import com.github.zafarkhaja.semver.Version
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.name.Named
import groovy.transform.CompileStatic
import org.restlet.Request
import org.restlet.Response
import restling.restlet.VersionRestlet

/**
 * A module that provides {@link Request} and {@link Response} implementations.
 */
@CompileStatic
class RequestResponseModule extends AbstractModule {

    Request request
    Response response

    @Inject
    @Named("default")
    Version defaultVersion

    @Override
    void configure() {
        bind(Request).toInstance(request)
        bind(Response).toInstance(response)

        Version specifiedVersion = request.attributes.get(VersionRestlet.VERSION_ATTRIBUTE_NAME) as Version
        bind(Version).toInstance(specifiedVersion ?: defaultVersion)
    }

}

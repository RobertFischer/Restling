package restling.restlet

import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.routing.Router
import org.restlet.routing.Template

/**
 * Router responsible for filtering out the "/v1", "/v2", etc., and setting the version.
 */
@CompileStatic
class RestlingVersionRouter extends Router {

    private final RestlingRouter next;

    RestlingVersionRouter(Context context, RestlingRouter next) {
        super(context)
        this.attach("/v{${VersionRestlet.VERSION_URL_ATTRIBUTE_NAME}}", new VersionRestlet(context, next)).with {
            matchingMode = Template.MODE_STARTS_WITH
        }
        this.attachDefault(next)
    }

}

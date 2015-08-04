package restling.restlet
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.routing.Router
/**
 * Router responsible for filtering out the "/v1", "/v2", etc., and setting the version.
 */
@CompileStatic
class RestlingVersionRouter extends Router {

    private final RestlingRouter next;


    RestlingVersionRouter(Context context, RestlingRouter next) {
        super(context)
        this.attach("/v{url_version}", new VersionRestlet(context, next))
        this.attachDefault(next)
    }

    //TODO Add JSemVer to the build
    //TODO Implement the version based on url
    //TODO Implement the version based on the header
    //TODO Implement a check that the url and version are related to each other

}

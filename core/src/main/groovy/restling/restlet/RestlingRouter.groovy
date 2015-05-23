package restling.restlet

import com.google.inject.Inject
import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.resource.Finder
import org.restlet.resource.ServerResource
import org.restlet.routing.Router

/**
 * Responsible for providing routing information into the Restling application.
 */
@CompileStatic
abstract class RestlingRouter extends Router {

    /**
     * The injector that will be used to create resource classes.
     */
    @Inject
    Injector injector;

    @Inject
    RestlingRouter(Context context) {
        super(context)
    }

    /**
     * Specifies that we use a {@link RestlingFinder}
     */
    Class<? extends Finder> finderClass = RestlingFinder

    /**
     * Constructs a {@link RestlingFinder} to provide an instance of {@code resourceClass}.
     *
     * @param resourceClass The class to be created
     * @return An instantiated and configured {@link RestlingFinder}
     */
    @Override
    Finder createFinder(Class<? extends ServerResource> resourceClass) {
        assert injector: "Need an injector in order to create the finder!"
        return new RestlingFinder(context, resourceClass, injector)
    }

    /**
     * This is the hook for performing initialization (such as calling {@code .attach})
     * for this router.
     */
    abstract void init() throws Exception;

}

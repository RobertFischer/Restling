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

    @Inject
    Injector injector

    /**
     * Default constructor. Equivalent to calling @{link RestlingRouter#RestlingRouter(Context)} and passing in
     * {@code null}.
     */
    RestlingRouter() {
        this(null);
    }

    /**
     * Constructor for if you have a context you want to pass in.
     *
     * @param context The context to install; may be {@code null}
     */
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
        assert injector: "Need an injector to create the finder!"
        assert context: "Need a context to create the finder!"
        return new RestlingFinder(context, resourceClass, injector)
    }

    /**
     * This is the hook for performing initialization (such as calling {@code .attach})
     * for this router. {@link #setContext(Context)} will be called with a non-null context
     * before this method is called.
     */
    abstract void init() throws Exception;

}

package restling.restlet

import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.resource.Finder
import org.restlet.resource.ServerResource
import org.restlet.routing.Filter
import org.restlet.routing.Router
import org.restlet.routing.Template
import org.restlet.routing.TemplateRoute
import restling.guice.RequestInjectorFactory

/**
 * Responsible for providing routing information into the Restling application.
 */
@CompileStatic
abstract class RestlingRouter extends Router {

    @Inject
    RequestInjectorFactory injectorFactory

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
        assert injectorFactory: "Need an injector to create the finder!"
        assert context: "Need a context to create the finder!"
        return new RestlingFinder(context, resourceClass, injectorFactory)
    }

    /**
     * Attaches a subrouter who is delegated to based on {@code pathPrefix}. If {@code subrouter}
     * is a {@code RestlingRouter}, then we will call {@link RestlingRouter#init()} on it before
     * we attach it.
     *
     * @param pathPrefix The path prefix that the subrouter will own.
     * @param subrouter The router to handle calls into that path prefix.
     * @return The route that was constructed.
     */
    TemplateRoute attachSubRouter(String pathPrefix, Class<? extends Router> subrouter) {
        assert pathPrefix: "Need a path prefix specifying where to attach the subrouter"
        assert subrouter: "Need a router to attach to the path $pathPrefix"
        assert injectorFactory: "Need an injector to construct the subrouter"
        assert context: "Need a context to provide to the newly-created subrouter for $pathPrefix"
        def r = new RestlingInjectedRouter(context, subrouter, injectorFactory)
        return this.attach(pathPrefix, r, Template.MODE_STARTS_WITH)
    }

    /**
     * Given a {@code path}, a {@code resource}, and a series of {@code filters}, attaches to {@code path}
     * each of the {@code filters}
     * (applied in the given sequence) terminating with {@code resource}. All of them are created by the
     * request injector.
     *
     * @param path The path to attach; may not be {@code null} or empty
     * @param resource The resource to terminate the chain with; may not be {@code null}
     * @param filters The filters to apply; may be {@code null} or {@code empty}
     * @return The attached route.
     */
    TemplateRoute attach(String path, Class<? extends ServerResource> resource, Class<? extends Filter>... filters) {
        assert path: "Need a path to attach a value"
        assert resource: "Need a resource to attach to ultimately"
        if (!filters) {
            return super.attach(path, resource)
        } else {
            assert context: "Need a context to create Restling Injected Filters"
            assert injectorFactory: "Need an injector factory to create Restling Injected Filters"
            def filterChain = (filters as List<Class<Filter>>).collect { filterClass ->
                new RestlingInjectedFilter(context, filterClass, injectorFactory)
            }
            def last = filterChain.inject(null as RestlingInjectedFilter, { p, f ->
                def prev = p as RestlingInjectedFilter
                def filter = f as RestlingInjectedFilter
                if (prev) prev.next = filter
                filter
            }) as RestlingInjectedFilter
            last.next = createFinder(resource)
            return super.attach(path, filterChain[0])
        }
    }

    /**
     * This is the hook for performing initialization (such as calling {@code .attach})
     * for this router. {@link #setContext(Context)} will be called with a non-null context
     * before this method is called.
     */
    abstract void init() throws Exception;

}

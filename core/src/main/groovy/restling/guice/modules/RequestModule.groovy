package restling.guice.modules

import com.google.inject.AbstractModule
import com.google.inject.Module

/**
 * The base classes for the {@link Module} that is loaded within a specific request context. Note that Restling will
 * automatically provide the {@link org.restlet.Request} and {@link org.restlet.Response} into the context. This class
 * can be extended to provide other resources that are available within the context of a request (eg: a database
 * connection).
 */
class RequestModule extends AbstractModule {

    /**
     * Perform the context configuration for this request. By default, this method does nothing.
     */
    @Override
    void configure() {}
}

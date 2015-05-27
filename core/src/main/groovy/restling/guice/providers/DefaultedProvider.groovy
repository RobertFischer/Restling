package restling.guice.providers

import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.Provider
import groovy.transform.CompileStatic
import restling.guice.RestlingDefault

/**
 * Provides the Restling default implementation for the given class, unless the user has explicitly bound
 * an implementation for the class.
 */
@CompileStatic
class DefaultedProvider<T> implements Provider<T> {

    /**
     * The class to instantiate.
     */
    final Class<T> target

    /**
     * The {@link Provider} of an {@link Injector}. The resulting injector is the one whose bindings will
     * be queried.
     */
    final Provider<Injector> injectorProvider

    DefaultedProvider(Class<T> target, Provider<Injector> injectorProvider) {
        assert target: "Need a target class to provide"
        assert injectorProvider: "Need a provider for the injector"
    }

    /**
     * Provides an instance of {@code T}. Must never return {@code null}.
     *
     * @throws OutOfScopeException when an attempt is made to access a scoped object while the scope
     *     in question is not currently active
     * @throws ProvisionException if an instance cannot be provided. Such exceptions include messages
     *     and throwables to describe why provision failed.
     */
    @Override
    T get() {
        def injector = injectorProvider.get()
        def binding = injector.getExistingBinding(Key.get(target));
        if (binding) return injector.getInstance(target);
        return injector.getInstance(Key.get(target, RestlingDefault))
    }
}

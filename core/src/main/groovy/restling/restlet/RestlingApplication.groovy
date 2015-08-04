package restling.restlet

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.restlet.Application
import org.restlet.Context
import org.restlet.Restlet
import org.restlet.engine.Engine
import org.restlet.engine.converter.ConverterHelper
import org.restlet.ext.jackson.JacksonConverter

/**
 * The container of all the Restling magic.
 */
@CompileStatic
class RestlingApplication extends Application {

    /**
     * The {@link Provider} used to {@link #createInboundRoot()}.
     */
    @Inject
    Provider<RestlingRouter> inboundRootProvider

    /**
     * Constructor (passes through to the superclass).
     *
     * {@inheritDoc}
     */
    @Inject
    RestlingApplication(Context context, JacksonConverter converter) {
        super(context)
        tunnelService.extensionsTunnel = true
        replaceConverter(JacksonConverter, converter)
    }

    @Override
    Restlet createInboundRoot() {
        // Create the user's inbound root
        assert inboundRootProvider: "please provide an inboundRootProvider"
        def router = inboundRootProvider.get()
        assert router: "the inbound root provider returned null"
        router.init()

        // Preface the inbound root with the version router
        def versionRouter = new RestlingVersionRouter(context, router)

        return versionRouter
    }

    // From http://restlet-discuss.1400322.n2.nabble.com/Jackson-Mix-in-Annotations-td6211060.html#a6231831
    /**
     * Registers a new converter with the Restlet engine, after removing
     * the first registered converter of the given class.
     */
    void replaceConverter(
            Class<? extends ConverterHelper> converterClass,
            ConverterHelper newConverter) {
        // Get the registered converters
        List<ConverterHelper> converters = Engine.instance.registeredConverters

        // Remove the old
        def oldConverter = converterService.find { converterClass == it.class }
        if (oldConverter) converters.remove(oldConverter)

        // Add the new
        converters.add(newConverter);

        // Let people know what just happened
        if (!oldConverter) {
            logger.info(String.format("Added {} to Restlet Engine", newConverter.getClass()));
        } else {
            logger.info(String.format("Replaced {} with {} in Restlet Engine", oldConverter.getClass(), newConverter.getClass()));
        }
    }


}

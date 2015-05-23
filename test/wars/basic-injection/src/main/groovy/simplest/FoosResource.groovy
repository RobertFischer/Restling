package simplest

import com.google.inject.Inject
import com.google.inject.name.Named
import org.restlet.resource.Get
import org.restlet.resource.ServerResource

class FoosResource extends ServerResource {

    @Inject
    @Named("message")
    String message

    @Get
    String doGet() {
        logger.info("Executing doGet() in ${this.class}")
        assert message: "No message provided!"
        return message
    }

}

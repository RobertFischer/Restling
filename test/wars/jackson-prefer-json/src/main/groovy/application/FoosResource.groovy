package application

import org.restlet.resource.Get
import org.restlet.resource.ServerResource

class FoosResource extends ServerResource {

    @Get
    Map<String, Boolean> doGet() {
        logger.info("Executing doGet() in ${this.class}")
        return ["hello": true]
    }

}

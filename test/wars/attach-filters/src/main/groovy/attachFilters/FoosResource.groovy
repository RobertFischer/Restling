package attachFilters

import org.restlet.resource.Get
import org.restlet.resource.ServerResource

class FoosResource extends ServerResource {

    @Get
    String doGet() {
        logger.info("Executing doGet() in ${this.class}")
        def message = request.getAttributes().get("FILTER_TEST").join(", ")
        return message
    }

}

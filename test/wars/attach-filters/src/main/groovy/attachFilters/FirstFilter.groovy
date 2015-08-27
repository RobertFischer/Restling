package attachFilters

import org.restlet.Request
import org.restlet.Response
import org.restlet.routing.Filter

/**
 * Created by RCFischer on 8/27/15.
 */
class FirstFilter extends Filter {

    @Override
    protected int beforeHandle(Request request, Response response) {
        request.getAttributes().put("FILTER_TEST", ["Hello"])
        return super.beforeHandle(request, response)
    }
}

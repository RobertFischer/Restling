package attachFilters

import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.restlet.Context
import restling.restlet.RestlingRouter

@CompileStatic
class SimplestRouter extends RestlingRouter {

    @Inject
    SimplestRouter(Context context) {
        super(context)
    }

    @Override
    void init() {
        attach("/foos/", FoosResource)
    }

}

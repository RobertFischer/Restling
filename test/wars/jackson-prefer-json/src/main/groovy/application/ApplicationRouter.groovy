package application

import com.google.inject.Inject
import groovy.transform.CompileStatic
import org.restlet.Context
import restling.restlet.RestlingRouter

@CompileStatic
class ApplicationRouter extends RestlingRouter {

    @Inject
    ApplicationRouter(Context context) {
        super(context)
    }

    @Override
    void init() {
        attach("/foos", FoosResource)
    }

}

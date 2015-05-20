package restling.test

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.*
import restling.test.model.WarDir

/**
 * Provides a servlet container for testing purposes, so that we can programmatically perform functional tests.
 */
@CompileStatic
@TupleConstructor
class TestingServletContainer {

    WarDir warDir
    int port = 8912

    private Server server

    void start() {
        assert !server || server.stopped: "Server already started!"

        assert warDir: "Please set the war directory (warDir)"

        // Based on https://github.com/jetty-project/embedded-servlet-3.0/blob/master/src/test/java/com/company/foo/EmbedMe.java
        server = new Server(port)
        WebAppContext context = new WebAppContext()
        context.with {
            resourceBase = warDir
            descriptor = warDir.webXml
            configurations = [
                    new WebXmlConfiguration(),
                    new WebInfConfiguration(),
                    new MetaInfConfiguration(),
                    new FragmentConfiguration()
            ] as Configuration[]
            contextPath = "/"
            parentLoaderPriority = true
        }
        server.handler = context
        server.start()
    }

    void stop() {
        if (!server || server.stopping || server.stopped) return

        server.stopAtShutdown
        server.stop()
    }

}

package restling.test

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.junit.After
import org.junit.Before
import restling.test.model.WarDir

@TupleConstructor
@CompileStatic
class WarProjectTests {

    String warProjectName
    TestingServletContainer server

    @Before
    void ensureProjectName() {
        assert warProjectName: "No war project name set"
        assert !warProjectName.isAllWhitespace(): "War project name is all whitespace"
    }

    WarDir getWarDir() {
        List<File> potentialRoots = [
                "src/test/wars/$warProjectName", "test/src/test/wars/$warProjectName"
        ].collect({ new File(it) })
        File warRoot = potentialRoots.find { it.exists() && it.directory }
        if (!warRoot) {
            throw new IllegalArgumentException("Could not find the root for $warProjectName")
        } else {
            return new WarDir(warRoot)
        }
    }

    @Before
    void startServer() {
        server = new TestingServletContainer(warDir)
        server.start()
    }

    @After
    void stopServer() {
        server?.stop()
        server = null
    }

}

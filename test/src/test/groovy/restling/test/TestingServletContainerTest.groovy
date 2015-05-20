package restling.test

import groovy.transform.CompileStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import restling.test.model.WarDir

@CompileStatic
class TestingServletContainerTest {

    WarDir warDir

    @Before
    void assignWarDir() {
        warDir = WarDir.createTemporaryWarDir()
    }

    @After
    void destroyWarDir() {
        warDir.delete()
        warDir = null
    }

    @Test
    void servletContainerSmokeTest() {
        TestingServletContainer container = new TestingServletContainer(warDir)
        container.start()
        container.stop()
    }

}

package restling.test.model

import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder

/**
 * Represents the root directory of an unpacked WAR
 */
class WarDir {

    final File root
    @Lazy
    File webInf = {
        if (!root) return null
        return new File(root, "WEB-INF")
    }()
    @Lazy
    File webXml = {
        if (!webInf) return null
        return new File(webInf, "web.xml")
    }()

    WarDir(String root) {
        this(root as File)
    }

    WarDir(File root) {
        assert root: "Please specify the root of the unpacked WAR (was null) "

        this.root = root
        assert root.exists(): "Root of war ($root.absolutePath) does not exist"
        assert root.directory: "Root of war ($root.absolutePath) is not a directory"

        assert webInf.exists(): "WEB-INF ($webInf.absolutePath) does not exist"
        assert webInf.directory: "WEB-INF ($webInf.absolutePath) is not a directory"

        assert webXml.exists(): "web.xml ($webXml.absolutePath) does not exist"
        assert webXml.file: "web.xml ($webXml.absolutePath) is not a regular file"
    }

    @CompileStatic
    void delete() {
        if (!root.deleteDir()) throw new IOException("Could not delete the directory $root.absolutePath")
    }

    static WarDir createTemporaryWarDir() {
        File tmpDir = File.createTempDir("restling", "war")
        Runtime.runtime.addShutdownHook({ tmpDir.deleteDir() })
        File webInf = new File(tmpDir, "WEB-INF")
        if (!webInf.mkdir()) throw new IOException("Could not create the directory $webInf")
        File webXml = new File(webInf, "web.xml")
        if (!webXml.createNewFile()) throw new IOException("Could not create the file at $webXml")
        webXml.withWriter { writer ->
            new MarkupBuilder(writer).'web-app'(
                    xmlns: 'http://java.sun.com/xml/ns/j2ee',
                    'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance",
                    'xsi:schemaLocation': "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd",
                    version: "3.0") {

            }
        }
        return new WarDir(tmpDir)
    }
}

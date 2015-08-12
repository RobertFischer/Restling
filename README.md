Restling
========

Restlet + Guice + Groovy...Made Simple.
--------------------------------------------------

[Restlet](http://restlet.com/products/restlet-framework/features/) itself is awesome, but can be somewhat intimidating to get into initially. It also follows
the standard Java convention of wide integration by providing integration points and requiring you to configure them. The downside is that this takes some
configuration, and that requires understanding of how to best do that configuration.

Restling groups together Groovy, Restlet, and Guice to create a smoother development experience for REST APIs. More importantly, it takes away the rough edges
from deploying Restlet in the way you almost certainly want to deploy it.

Advantages
--------------

  * You get to use Restlet as a servlet without having to wire things together yourself.
  * You get to use Guice's type-safe dependency injection (with its awesome error handling) for your all your `ServerResource` instances.
  * You get to use Groovy's wonderfully succinct syntax for coding up those `ServerResource` instances.
  * You get versioning for your API for free
  * Various sane configurations are now the defaults, such as:
    * A strong preference for JSON. If there is no other specific type requested, respond with JSON.
    * Use file extensions (eg: `.json`, `.xml`) to determine what file type is desired.
    * Data conversion configuration which is more secure, more what you want by default, and more extensible.
    * The JSON serialization support you want: JDK8 types (including `Optional`), Guava Collection types, and Joda types.
    * SLF4J logging support out of the box

Usage
---------------

Define a class extending `RestlingApplicationModule` in your war. This class signature specifies all the configuration that you need to provide for Restling,
and it also exposes the key optional hooks for Guice injection. The primary things you will have to define are:

  * A `RestlingRouter`, which extends the [Restlet Router](http://restlet.com/technical-resources/restlet-framework/guide/2.3/core/routing/hierarchical-uris),
    and tells the Restling application how to direct traffic.
  * A Guice configuration extending `RestlingApplicationModule` for its dependency injection.

Let's say you call your application module `com.foo.BarModule`.  Given that, create your `web.xml` like this:

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">
  <servlet>
    <servlet-name>restling</servlet-name>
    <servlet-class>restling.RestlingServlet</servlet-class>
    <init-param>
      <param-name>guice-module</param-name>
      <param-value>com.foo.BarModule</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>restling</servlet-name>
    <url-pattern>/*</url-pattern>
  </servlet-mapping>
</web-app>
```

Then war it up, being sure to including the Restling library and its dependencies in your war. Now deploy and enjoy!

Versioning
------------

In your initial development stages, you can safely ignore versioning for your API. Sooner or later, however, you will want it. Restling leverages the
[JSemVer](https://github.com/zafarkhaja/jsemver) library for representing versions.

The user specifies a version either through the URL or through an HTTP header.  The HTTP header is `API-VERSION`, and the value is supposed to be the version to use.
In the URL, versioning is specified by the root pattern of `/v([\w\.]+)/`, and the captured pattern is supposed to be the version to use. If neither of those means
are specified, the default version from `RestlingApplicationModule#defaultVersion` is used.

You can get access to the user-requested API version by using `@Inject Version` in your class. JSemVer provides a nice API for performing comparisons, and you can
apply your specific logic as you would like using those methods.

Version parsing rules are defined by JSemVer, with the added capability of specifying partial versions like `1` and
`1.2`. (For the conversation around not accepting partial versions, see
[this conversation on GitHub](https://github.com/zafarkhaja/jsemver/issues/15?_pjax=%23js-repo-pjax-container#issuecomment-68672473).)


Extended Usage Tips
--------------------

[Jackson's parameter name deserialization](https://github.com/FasterXML/jackson-module-parameter-names) is insanely helpful, and comes installed with Restling.
It's really what you want:
if you give parameter names to your class's constructor, they are mapped to field names, which enables you to have `final` fields (among other awesomeness).
In order to use it, however, you need to pass `-parameters` to your project's `javac` compiler. Consult your build system documentation for how to do this.

Take advantage of Groovy's [`@Slf4j`](http://docs.groovy-lang.org/latest/html/api/groovy/util/logging/Slf4j.html) annotation, along with `@CompileStatic`, and
you get all the nice logging code and type checking you could want.

Application Module Protip
---------------------------

Groovy makes the application module source much nicer, especially if you use the
[`@CompileStatic`](http://docs.groovy-lang.org/latest/html/gapi/groovy/transform/CompileStatic.html) annotation to retain Guice's very useful type constraints:
you then get the best of Groovy's dynamism and Guice's type safety!

Examples
-----------

Examples (which are used as tests in the framework, and therefore guaranteed to actually work and be up-to-date) are in `./test/wars`. See the `README.md` files
in each directory for details. The `basic-injection` example war is a good example of a minimal configuration with injection used on a server resource.

FAQ
-------

  * *What if I want to have multiple modules?* <br />Have your application module call `binder.install(module)` ([API docs](http://google.github.io/guice/api-docs/latest/javadoc/com/google/inject/Binder.html#install-com.google.inject.Module-)).

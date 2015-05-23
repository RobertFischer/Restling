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
  * You get to use Guice's type-safe dependency injection and gets its awesome error messages for your all your {@code ServerResource} instances.
  * You get to use Groovy's wonderfully succinct syntax for coding up those {@code ServerResource} instances.
  * Various sane configurations are now the defaults, such as:
    * Assuming you want to respond with JSON unless there is a file suffix specified

Usage
---------------

Define a class extending `RestlingApplicationModule` in your war. This class signature specifies all the configuration that you need to provide for Restling,
and it also exposes the key optional hooks for Guice injection. The primary things you will have to define are:

  * A `RestlingRouter`, which extends the [Restlet Router](http://restlet.com/technical-resources/restlet-framework/guide/2.3/core/routing/hierarchical-uris),
    and tells the Restling application how to direct traffic.
  * Any Guice configuration for its dependency injection.

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

Application Module Protip
---------------------------

Groovy makes the application module source much nicer, especially if you use the
[`@CompileStatic`](http://docs.groovy-lang.org/latest/html/gapi/groovy/transform/CompileStatic.html) annotation to retain Guice's very useful type constraints:
you then get the best of Groovy's dynamism and Guice's type safety!

Also, the [Groovy `with` method](http://mrhaki.blogspot.com/2009/09/groovy-goodness-with-method.html)
is really handy for working with the `binder` argument in your application module:

```groovy
    @Override
    void configureCustomBindings(Binder binder) {
        binder.with {
          bindConstant().annotatedWith(Names.named("message")).to("Hello, World!")
          bind(MyClass)
        }
    }
```

Examples
-----------

Examples (which are used as tests in the framework, and therefore guaranteed to actually work and be up-to-date) are in `./test/wars`. See the `README.md` files
in each directory for details. The `basic-injection` example war is a good example of a minimal configuration with injection used on a server resource.

FAQ
-------

  * *What if I want to have multiple modules?* Have your application module call `binder.install(module)` ([API docs](http://google.github.io/guice/api-docs/latest/javadoc/com/google/inject/Binder.html#install-com.google.inject.Module-)).

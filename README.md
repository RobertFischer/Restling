Restling
========

Restlet + Guice + Groovy...Made Simple.
--------------------------------------------------

[Restlet](http://restlet.com/products/restlet-framework/features/) itself is awesome, but can be somewhat intimidating to get into initially. It also follows
the standard Java convention of wide integration by providing integration points and requiring you to configure them. The downside is that this takes some
configuration, and that requires understanding of how to best do that configuration.

Restling groups together Groovy, Restlet, and Guice to create a smoother development experience for REST APIs. More importantly, it takes away the rough edges
from deploying Restlet in the way you almost certainly want to deploy it.

Usage
---------------

Define your applications's [Guice module](https://github.com/google/guice/wiki/GettingStarted) as a class in your war. Let's say you call it `com.foo.BarModule`.
This module should provide the following keys:

  * A [Restlet Router](http://restlet.com/technical-resources/restlet-framework/guide/2.3/core/routing/hierarchical-uris) that will be used as the
    inbound root for the
    [Restlet Application](http://restlet.com/technical-resources/restlet-framework/javadocs/3.0/jee/api/org/restlet/Application.html?is-external=true)
    (ie: where all the calls from clients will be routed to).

Once you have that, create your `web.xml` like this:

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
    <url-pattern><!-- This empty url-pattern will match the context where the webapp is mounted --></url-pattern>
  </servlet-mapping>
</web-app>
```

Then war it up, being sure to including the Restling library and its dependencies in your war. Now deploy and enjoy!

Examples
-----------

Examples (which are used as tests in the framework, and therefore guaranteed to actually work and be up-to-date) are in `./test/wars`. See the `README.md` files
in each directory for details.

FAQ
-------

  * *What if I want to have multiple modules?* Have your application module call `binder.install(module)` ([API docs](http://google.github.io/guice/api-docs/latest/javadoc/com/google/inject/Binder.html#install-com.google.inject.Module-)).

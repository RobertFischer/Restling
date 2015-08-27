package restling.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.restlet.Request;
import org.restlet.Response;
import restling.guice.modules.RequestModule;
import restling.guice.modules.RequestResponseModule;

import java.util.*;

/**
 * Creates an injector for a request/response pair. The injector is cached on the request, so that multiple
 * calls to this class will not produce multiple injectors.
 */
public class RequestInjectorFactory {

  private static final String INJECTOR_ATTRIBUTE_NAME = RequestInjectorFactory.class.getName() + ".injector";

  private final Injector parent;

  @Inject
  public RequestInjectorFactory(Injector parent) {
    Objects.requireNonNull(parent, "Need a parent injector!");
    this.parent = parent;
  }

  /**
   * Searches for the injector attached to the request attributes. If not found, creates a new
   * injector based on the given {@code req} and {@code res} and attaches it to the request attributes.
   */
  public Injector create(Request req, Response res) {
    return (Injector) req.getAttributes().computeIfAbsent(INJECTOR_ATTRIBUTE_NAME, attrName -> {
      RequestResponseModule requestResponseModule = parent.getInstance(RequestResponseModule.class);
      requestResponseModule.setRequest(req);
      requestResponseModule.setResponse(res);

      Injector requestInjector = parent.createChildInjector(requestResponseModule);
      return requestInjector.createChildInjector(requestInjector.getInstance(RequestModule.class));
    });
  }

  /**
   * Provides the parent injector, which should be the Restling application injector.
   */
  public final Injector getParent() {
    return parent;
  }

}

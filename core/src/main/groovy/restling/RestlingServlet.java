package restling;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.servlet.ServletAdapter;
import org.restlet.routing.Router;
import restling.guice.RestlingModule;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * This is the servlet that you will put in your {@code web.xml} file. It will look
 * for your application's Guice module using {@link #getApplicationModule(ServletConfig)},
 * and then it will use that annotation to fetch an instance of the {@link Application} type
 * annotated with {@link InboundRoot}. That instance will be the Restlet that all requests
 * are routed into.
 */
public class RestlingServlet extends javax.servlet.http.HttpServlet {

  private ServletAdapter adapter;

  /**
   * Performs the bulk of the work.
   *
   * @param config The configuration used to configure the servlet; may not be {@code null}
   * @throws ServletException If something goes wrong
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    this.adapter = new ServletAdapter(getServletContext());
    RestlingApplication application = new RestlingApplication(adapter.getContext());
    Module applicationModule = getApplicationModule(config);
    Injector injector = Guice.createInjector(
                                                new RestlingModule(application),
                                                applicationModule
    );
    application.setInboundRootProvider(injector.getProvider(Router.class));
    this.adapter.setNext(application);
  }

  /**
   * Delegates servicing to to the retrieved {@link Restlet} (the one annotated with {@link InboundRoot}).
   */
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    this.adapter.service(req, resp);
  }

  /**
   * Responsible for providing the application's Guice {@link Module}. This module is
   * responsible for producing.
   * <p/>
   * By default, the implementation looks for the {@code guice-module} init parameter on the
   * {@link ServletConfig}, and instantiates that class through {@link Class#newInstance()}
   * after looking it up using the classloader from {@link ServletContext#getClassLoader()}.
   *
   * @param config The configuration to read from; may not be {@code null}
   * @return The Guice module for the application; may not be {@code null}
   * @throws ServletException If we were unable to construct the Guice module for the application
   */
  public Module getApplicationModule(ServletConfig config) throws ServletException {
    Objects.requireNonNull(config, "ServletConfig");
    String moduleName = config.getInitParameter("guice-module");
    if (moduleName == null || moduleName.isEmpty()) {
      throw new ServletException("Missing 'guice-module' init parameter " +
                                     "(should be the class name of the application Guice module)");
    }
    try {
      return instantiateModule((Class<Module>) getServletContext().getClassLoader().loadClass(moduleName));
    } catch (Exception e) {
      throw new ServletException("Could not instantiate Guice module from: " + moduleName, e);
    }
  }

  /**
   * Responsible for returning a {@link Module} given a class name for it. Currently, this
   * just calls {@link Class#newInstance()}, but future versions may provide more broad support.
   *
   * @param toInstantiate The class to instantiate; may not be {@code null}.
   * @return The instantiated module.
   */
  public Module instantiateModule(Class<Module> toInstantiate) throws ServletException {
    Objects.requireNonNull(toInstantiate, "class to instantiate");
    try {
      return toInstantiate.newInstance();
    } catch (Exception e) {
      throw new ServletException("Could not instantiate Guice module from " + toInstantiate, e);
    }
  }

}

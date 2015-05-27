package restling;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.restlet.ext.servlet.ServletAdapter;
import restling.guice.RestlingApplicationModule;
import restling.guice.RestlingModule;
import restling.restlet.MediaTypePreferenceFilter;
import restling.restlet.RestlingApplication;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * This is the servlet that you will put in your {@code web.xml} file. It will look
 * for your application's Guice module using {@link #getApplicationModuleClass(ServletConfig)},
 * and then it will use that module to construct an instance of the {@link RestlingApplication}.
 * That instance will be the Restlet that all requests are routed into.
 */
public class RestlingServlet extends javax.servlet.http.HttpServlet {

  private ServletAdapter adapter;

  /**
   * Performs the bulk of the work, including creating the {@link RestlingApplication}
   * and wiring it into the servlet.
   *
   * @param config The configuration used to configure the servlet; may not be {@code null}
   * @throws ServletException If something goes wrong
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    // Construct the servlet adapter that we are going to use
    this.adapter = new ServletAdapter(getServletContext());

    // Construct a filter to make JSON our preferred mode of communication
    MediaTypePreferenceFilter preferencesFilter = new MediaTypePreferenceFilter(this.adapter.getContext());
    this.adapter.setNext(preferencesFilter);

    // Construct the application
    Module restlingModule = new RestlingModule(preferencesFilter.getContext(), getApplicationModuleClass(getServletConfig()));
    Injector injector = Guice.createInjector(restlingModule);
    Module applicationModule = injector.getInstance(RestlingApplicationModule.class);
    injector = Guice.createInjector(restlingModule, applicationModule); // Child injector broke basic-injection tests
    RestlingApplication application = injector.getInstance(RestlingApplication.class);
    preferencesFilter.setNext(application);
  }

  /**
   * Undoes the work that was done in {@link #init(ServletConfig)}, so that we clean up after ourselves.
   */
  @Override
  public void destroy() {
    this.adapter = null;
    super.destroy();
  }

  /**
   * Delegates servicing to to the retrieved {@link RestlingApplication} after applying any necessary
   * wrappers.
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
  public Class<? extends RestlingApplicationModule> getApplicationModuleClass(ServletConfig config) throws ServletException {
    Objects.requireNonNull(config, "ServletConfig");
    String moduleName = config.getInitParameter("guice-module");
    if (moduleName == null || moduleName.isEmpty()) {
      throw new ServletException("Missing 'guice-module' init parameter " +
                                     "(should be the class name of the application Guice module)");
    }
    try {
      return (Class<? extends RestlingApplicationModule>) getServletContext().getClassLoader().loadClass(moduleName);
    } catch (Exception e) {
      throw new ServletException("Could not specify RestlingApplicationModule: " + moduleName, e);
    }
  }


}

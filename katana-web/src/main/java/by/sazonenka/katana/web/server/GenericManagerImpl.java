package by.sazonenka.katana.web.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Aliaksandr Sazonenka
 */
public abstract class GenericManagerImpl
    extends RemoteServiceServlet
    implements ServletContextAware {

  private static final long serialVersionUID = 2516553976328226703L;

  private static final Logger LOG = LoggerFactory.getLogger(GenericManagerImpl.class);

  private ServletContext servletContext;

  protected ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    LOG.debug("Processing handleRequest before.");
    doPost(request, response);
    LOG.debug("Processing handleRequest after.");

    return null;
  }

  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  protected Exception wrapUnexpected(Throwable unexpected) {
    Exception expected = new Exception(unexpected.getMessage());
    expected.setStackTrace(unexpected.getStackTrace());
    return expected;
  }

}

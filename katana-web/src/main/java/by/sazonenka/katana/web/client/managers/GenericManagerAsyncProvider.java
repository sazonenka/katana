package by.sazonenka.katana.web.client.managers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Provider;

/**
 * @author Aliaksandr Sazonenka
 */
public abstract class GenericManagerAsyncProvider<I> implements Provider<I> {

  @Override
  public I get() {
    I manager = getManager();

    ServiceDefTarget endpoint = (ServiceDefTarget) manager;
    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + getManagerEndpointURI());

    return manager;
  }

  protected abstract I getManager();
  protected abstract String getManagerEndpointURI();

}

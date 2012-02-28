package by.sazonenka.katana.web.client.managers;

import com.google.gwt.core.client.GWT;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFieldManagerAsyncProvider
    extends GenericManagerAsyncProvider<OutputFieldManagerAsync> {

  @Override
  protected OutputFieldManagerAsync getManager() {
    return GWT.create(OutputFieldManager.class);
  }

  @Override
  protected String getManagerEndpointURI() {
    return ManagerEndpoints.OUTPUT_FIELD_MANAGER_URI;
  }

}

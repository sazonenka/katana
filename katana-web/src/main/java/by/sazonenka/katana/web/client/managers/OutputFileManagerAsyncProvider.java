package by.sazonenka.katana.web.client.managers;

import com.google.gwt.core.client.GWT;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFileManagerAsyncProvider
    extends GenericManagerAsyncProvider<OutputFileManagerAsync> {

  @Override
  protected OutputFileManagerAsync getManager() {
    return GWT.create(OutputFileManager.class);
  }

  @Override
  protected String getManagerEndpointURI() {
    return ManagerEndpoints.OUTPUT_FILE_MANAGER_URI;
  }

}

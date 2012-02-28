package by.sazonenka.katana.web.client.managers;

import com.google.gwt.core.client.GWT;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConstraintConfigManagerAsyncProvider
    extends GenericManagerAsyncProvider<ConstraintConfigManagerAsync> {

  @Override
  protected ConstraintConfigManagerAsync getManager() {
    return GWT.create(ConstraintConfigManager.class);
  }

  @Override
  protected String getManagerEndpointURI() {
    return ManagerEndpoints.CONSTRAINT_CONFIG_MANAGER_URI;
  }

}

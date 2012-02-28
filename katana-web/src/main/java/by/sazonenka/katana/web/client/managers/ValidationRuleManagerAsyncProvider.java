package by.sazonenka.katana.web.client.managers;

import com.google.gwt.core.client.GWT;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ValidationRuleManagerAsyncProvider
    extends GenericManagerAsyncProvider<ValidationRuleManagerAsync> {

  @Override
  protected ValidationRuleManagerAsync getManager() {
    return GWT.create(ValidationRuleManager.class);
  }

  @Override
  protected String getManagerEndpointURI() {
    return ManagerEndpoints.VALIDATION_RULE_MANAGER_URI;
  }

}

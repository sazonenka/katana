package by.sazonenka.katana.web.client;

import by.sazonenka.katana.web.client.config.ConfigInjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConfigApplication implements EntryPoint {

  @Override
  public void onModuleLoad() {
    ConfigInjector injector = GWT.create(ConfigInjector.class);
    RootPanel.get("container").add(injector.getConfigTabPanel());
  }

}

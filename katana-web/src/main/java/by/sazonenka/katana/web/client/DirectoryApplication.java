package by.sazonenka.katana.web.client;

import by.sazonenka.katana.web.client.directory.DirectoryInjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Aliaksandr Sazonenka
 */
public final class DirectoryApplication implements EntryPoint {

  @Override
  public void onModuleLoad() {
    DirectoryInjector injector = GWT.create(DirectoryInjector.class);
    RootPanel.get("container").add(injector.getConfigDirectoryGrid());
  }

}

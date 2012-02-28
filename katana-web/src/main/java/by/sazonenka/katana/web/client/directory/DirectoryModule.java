package by.sazonenka.katana.web.client.directory;

import by.sazonenka.katana.web.client.CommonGinModule;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
public final class DirectoryModule extends AbstractGinModule {

  @Override
  protected void configure() {
    install(new CommonGinModule());

    bind(DirectoryGrid.class).in(Singleton.class);
  }

}

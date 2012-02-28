package by.sazonenka.katana.web.client.config;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author Aliaksandr Sazonenka
 */
@GinModules(ConfigModule.class)
public interface ConfigInjector extends Ginjector {

  ConfigTabPanel getConfigTabPanel();

}

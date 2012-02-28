package by.sazonenka.katana.web.client.directory;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author Aliaksandr Sazonenka
 */
@GinModules(DirectoryModule.class)
public interface DirectoryInjector extends Ginjector {

  DirectoryGrid getConfigDirectoryGrid();

}

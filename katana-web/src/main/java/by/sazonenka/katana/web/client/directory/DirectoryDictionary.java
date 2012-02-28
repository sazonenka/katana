package by.sazonenka.katana.web.client.directory;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class DirectoryDictionary {

  private final String contextPath;

  @Inject
  public DirectoryDictionary() {
    Dictionary dictionary = Dictionary.getDictionary("directoryDictionary");

    this.contextPath = dictionary.get("contextPath");
  }

  public String getContextPath() { return contextPath; }

}

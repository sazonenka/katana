package by.sazonenka.katana.web.client.config;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class ConfigDictionary {

  private final Long configId;

  @Inject
  public ConfigDictionary() {
    Dictionary dictionary = Dictionary.getDictionary("configDictionary");

    this.configId = Long.valueOf(dictionary.get("configId"));
  }

  public Long getConfigId() { return configId; }

}

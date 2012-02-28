package by.sazonenka.katana.web.client.config;

import java.util.Map;

import by.sazonenka.katana.web.client.CommonGinModule;
import by.sazonenka.katana.web.client.config.file.FilesTreeStoreProvider;
import by.sazonenka.katana.web.client.config.rule.RuleForm;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.common.collect.Maps;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConfigModule extends AbstractGinModule {

  @Override
  protected void configure() {
    install(new CommonGinModule());

    bind(ConfigTabPanel.class).in(Singleton.class);

    bind(new TypeLiteral<TreeStore<FilesTreeModel>>() { })
        .toProvider(FilesTreeStoreProvider.class)
        .in(Singleton.class);
  }

  @Provides @Singleton
  public ListStore<ValidationRuleModel> provideRuleListStore() {
    return new ListStore<ValidationRuleModel>();
  }

  @Provides @Singleton
  public Map<ValidationRuleModel, RuleForm> provideRuleFormStore() {
    return Maps.newLinkedHashMap();
  }

}

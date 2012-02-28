package by.sazonenka.katana.web.client;

import by.sazonenka.katana.web.client.managers.ConstraintConfigManagerAsync;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManagerAsyncProvider;
import by.sazonenka.katana.web.client.managers.OutputFieldManagerAsync;
import by.sazonenka.katana.web.client.managers.OutputFieldManagerAsyncProvider;
import by.sazonenka.katana.web.client.managers.OutputFileManagerAsync;
import by.sazonenka.katana.web.client.managers.OutputFileManagerAsyncProvider;
import by.sazonenka.katana.web.client.managers.ValidationRuleManagerAsync;
import by.sazonenka.katana.web.client.managers.ValidationRuleManagerAsyncProvider;
import by.sazonenka.katana.web.client.resources.Icons;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
public final class CommonGinModule extends AbstractGinModule {

  @Override
  protected void configure() {
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    bind(Icons.class).in(Singleton.class);

    bind(ConstraintConfigManagerAsync.class)
        .toProvider(ConstraintConfigManagerAsyncProvider.class)
        .in(Singleton.class);
    bind(ValidationRuleManagerAsync.class)
        .toProvider(ValidationRuleManagerAsyncProvider.class)
        .in(Singleton.class);
    bind(OutputFileManagerAsync.class)
        .toProvider(OutputFileManagerAsyncProvider.class)
        .in(Singleton.class);
    bind(OutputFieldManagerAsync.class)
        .toProvider(OutputFieldManagerAsyncProvider.class)
        .in(Singleton.class);
  }

}

package by.sazonenka.katana.web.client.directory;

import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class ConfigEditorPopupBuilder {

  private final EventBus eventBus;

  @Inject
  public ConfigEditorPopupBuilder(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  public ConfigEditorPopup build() {
    return new ConfigEditorPopup(eventBus);
  }

  public ConfigEditorPopup build(ConstraintConfigModel model) {
    return new ConfigEditorPopup(eventBus, model);
  }

}

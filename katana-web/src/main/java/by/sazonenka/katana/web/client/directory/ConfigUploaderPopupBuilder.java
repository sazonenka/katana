package by.sazonenka.katana.web.client.directory;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class ConfigUploaderPopupBuilder {

  private final EventBus eventBus;

  @Inject
  public ConfigUploaderPopupBuilder(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  public ConfigUploaderPopup build() {
    return new ConfigUploaderPopup(eventBus);
  }

}

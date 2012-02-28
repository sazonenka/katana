package by.sazonenka.katana.web.client.config.file;

import by.sazonenka.katana.web.model.FilesTreeModel;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class FileEditorPopupBuilder {

  private final EventBus eventBus;

  @Inject
  public FileEditorPopupBuilder(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  public FileEditorPopup build(FilesTreeModel model) {
    return new FileEditorPopup(eventBus, model);
  }

}

package by.sazonenka.katana.web.client.directory;

import java.util.Date;
import java.util.List;

import by.sazonenka.katana.web.client.events.ConstraintConfigDelete;
import by.sazonenka.katana.web.client.events.ConstraintConfigDownload;
import by.sazonenka.katana.web.client.events.ConstraintConfigEdit;
import by.sazonenka.katana.web.client.events.ConstraintConfigLoad;
import by.sazonenka.katana.web.client.events.ConstraintConfigOpen;
import by.sazonenka.katana.web.client.events.ConstraintConfigSave;
import by.sazonenka.katana.web.client.events.ConstraintConfigUpload;
import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManagerAsync;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class DirectoryEventHandlers {

  private final ConfigEditorPopupBuilder configEditorPopupBuilder;

  private final EventBus eventBus;
  private final DirectoryDictionary dictionary;

  private final ConstraintConfigManagerAsync configManager;

  @Inject
  public DirectoryEventHandlers(ConfigEditorPopupBuilder configEditorPopupBuilder,
      EventBus eventBus, DirectoryDictionary dictionary,
      ConstraintConfigManagerAsync configManager) {
    this.configEditorPopupBuilder = Preconditions.checkNotNull(configEditorPopupBuilder);

    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.dictionary = Preconditions.checkNotNull(dictionary);

    this.configManager = Preconditions.checkNotNull(configManager);
  }

  public void attach(final Grid<ConstraintConfigModel> grid) {
    final ListStore<ConstraintConfigModel> store = grid.getStore();
    final ColumnModel cm = grid.getColumnModel();

    eventBus.addHandler(ConstraintConfigLoad.Event.getType(),
        new ConstraintConfigLoad.Handler() {
          @Override
          public void onLoad(final ConstraintConfigLoad.Event event) {
            configManager.findAll(new GenericAsyncCallback<List<ConstraintConfigModel>>() {
              @Override
              public void onSuccess(List<ConstraintConfigModel> result) {
                event.getCallback().onSuccess(result);
              }
            });
          }
        });

    eventBus.addHandler(ConstraintConfigUpload.Event.getType(),
        new ConstraintConfigUpload.Handler() {
          @Override
          public void onUpload(ConstraintConfigUpload.Event event) {
            Window.Location.reload();
          }
        });

    eventBus.addHandler(ConstraintConfigOpen.Event.getType(),
        new ConstraintConfigOpen.Handler() {
          @Override
          public void onOpen(ConstraintConfigOpen.Event event) {
            Window.open(dictionary.getContextPath() + "config.htm?id=" + event.getModel().getId(),
                "_blank", null);
          }
        });

    eventBus.addHandler(ConstraintConfigDownload.Event.getType(),
        new ConstraintConfigDownload.Handler() {
          @Override
          public void onDownload(ConstraintConfigDownload.Event event) {
            Window.open(dictionary.getContextPath() + "download.htm?id=" + event.getModel().getId(),
                "_blank", null);
          }
        });

    eventBus.addHandler(ConstraintConfigEdit.Event.getType(),
        new ConstraintConfigEdit.Handler() {
          @Override
          public void onEdit(ConstraintConfigEdit.Event event) {
            configEditorPopupBuilder.build(event.getModel()).show();
          }
        });

    eventBus.addHandler(ConstraintConfigSave.Event.getType(),
        new ConstraintConfigSave.Handler() {
          @Override
          public void onSave(ConstraintConfigSave.Event event) {
            ConstraintConfigModel model = event.getModel();
            model.setModified(new Date());

            configManager.save(model, new GenericAsyncCallback<ConstraintConfigModel>() {
              @Override
              public void onSuccess(ConstraintConfigModel result) {
                Info.display("Success", "The config has been saved");

                if (store.contains(result)) {
                  store.remove(result);
                }
                store.insert(result, 0);

                grid.reconfigure(store, cm);
                grid.getSelectionModel().select(result, false);
              }
            });
          }
        });

    eventBus.addHandler(ConstraintConfigDelete.Event.getType(),
        new ConstraintConfigDelete.Handler() {
          @Override
          public void onDelete(final ConstraintConfigDelete.Event event) {
            MessageBox.confirm("Delete", "Do you really want to delete the config?",
                new Listener<MessageBoxEvent>() {
                  @Override
                  public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getText().equals("Yes")) {
                      final ConstraintConfigModel model = event.getModel();

                      configManager.delete(model.getId(), new GenericAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                          Info.display("Success", "The config has been deleted");

                          store.remove(model);
                          grid.reconfigure(store, cm);
                        }
                      });
                    }
                  }
                });
          }
        });
  }

}

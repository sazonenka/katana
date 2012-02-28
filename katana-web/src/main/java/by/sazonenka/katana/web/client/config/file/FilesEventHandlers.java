package by.sazonenka.katana.web.client.config.file;

import java.util.List;

import by.sazonenka.katana.web.client.config.ConfigDictionary;
import by.sazonenka.katana.web.client.config.ConfigWarningTracker;
import by.sazonenka.katana.web.client.events.OutputFieldAdd;
import by.sazonenka.katana.web.client.events.OutputFieldMapToRule;
import by.sazonenka.katana.web.client.events.OutputFieldRefresh;
import by.sazonenka.katana.web.client.events.OutputFieldReorder;
import by.sazonenka.katana.web.client.events.OutputFieldSave;
import by.sazonenka.katana.web.client.events.OutputFileCollapseAll;
import by.sazonenka.katana.web.client.events.OutputFileDelete;
import by.sazonenka.katana.web.client.events.OutputFileExpandAll;
import by.sazonenka.katana.web.client.events.OutputFileMapToParentFile;
import by.sazonenka.katana.web.client.events.OutputFileRefresh;
import by.sazonenka.katana.web.client.events.OutputFileRename;
import by.sazonenka.katana.web.client.events.OutputFileReorder;
import by.sazonenka.katana.web.client.events.OutputFileSave;
import by.sazonenka.katana.web.client.events.OutputFileUnmap;
import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.OutputFieldManagerAsync;
import by.sazonenka.katana.web.client.managers.OutputFileManagerAsync;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class FilesEventHandlers {

  private final FileEditorPopupBuilder popupBuilder;

  private final EventBus eventBus;
  private final ConfigDictionary dictionary;
  private final ConfigWarningTracker warningTracker;

  private final OutputFileManagerAsync fileManager;
  private final OutputFieldManagerAsync fieldManager;

  @Inject
  public FilesEventHandlers(FileEditorPopupBuilder popupBuilder,
      EventBus eventBus,
      ConfigDictionary dictionary,
      ConfigWarningTracker warningTracker,
      OutputFileManagerAsync fileManager,
      OutputFieldManagerAsync fieldManager) {
    this.popupBuilder = Preconditions.checkNotNull(popupBuilder);

    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.dictionary = Preconditions.checkNotNull(dictionary);
    this.warningTracker = Preconditions.checkNotNull(warningTracker);

    this.fileManager = Preconditions.checkNotNull(fileManager);
    this.fieldManager = Preconditions.checkNotNull(fieldManager);
  }

  public void attach(final FilesTree filesTree) {
    final TreeStore<FilesTreeModel> store = filesTree.getStore();

    eventBus.addHandler(OutputFileSave.Event.getType(),
        new OutputFileSave.Handler() {
          @Override
          public void onSave(OutputFileSave.Event event) {
            OutputFileModel model = event.getModel();
            model.setConfig(dictionary.getConfigId());

            fileManager.save(model, new GenericAsyncCallback<OutputFileModel>() {
              @Override
              public void onSuccess(OutputFileModel result) {
                Info.display("Success", "The file has been saved");

                if (!store.contains(result)) {
                  store.add(result, false);
                } else {
                  store.update(result);

                  eventBus.fireEvent(new OutputFileRefresh.Event());
                }
                updateWarningState();
              }
            });
          }
        });

    eventBus.addHandler(OutputFileRename.Event.getType(),
        new OutputFileRename.Handler() {
          @Override
          public void onRename(OutputFileRename.Event event) {
            List<FilesTreeModel> selection = filesTree.getSelectionModel().getSelection();
            if (selection.size() != 1) {
              MessageBox.alert("Warning", "Please, select exactly one file or field", null);
              return;
            }

            FilesTreeModel model = selection.get(0);
            popupBuilder.build(model).show();
          }
        });

    eventBus.addHandler(OutputFieldMapToRule.Event.getType(),
        new OutputFieldMapToRule.Handler() {
          @Override
          public void onMap(OutputFieldMapToRule.Event event) {
            final List<OutputFieldModel> fields = event.getFields();
            List<Long> fieldIds = extractIds(fields);

            final ValidationRuleModel rule = event.getRule();

            fieldManager.map(fieldIds, rule.getId(), new GenericAsyncCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                for (OutputFieldModel field : fields) {
                  field.setRule(rule.getName());
                  store.update(field);
                }
              }
            });
          }
        });

    eventBus.addHandler(OutputFileMapToParentFile.Event.getType(),
        new OutputFileMapToParentFile.Handler() {
          @Override
          public void onMap(OutputFileMapToParentFile.Event event) {
            final OutputFileModel parent = event.getParent();
            if (parent.getParentFile() != null) {
              MessageBox.alert("Warning",
                  "The file '" + parent.getName() + "' has a 'parent'!"
                      + " You can't extend a file that extends other file!",
                  null);
              return;
            }

            final List<OutputFileModel> files = event.getFiles();
            List<Long> fileIds = extractIds(files);

            fileManager.map(fileIds, parent.getId(), new GenericAsyncCallback<Boolean>() {
              @Override
              public void onSuccess(Boolean result) {
                if (result) {
                  for (OutputFileModel file : files) {
                    file.setParentFile(parent.getName());
                    store.update(file);
                  }
                } else {
                  MessageBox.alert("Warning",
                      "The file has 'children'! You can't extend a file that is already extended!",
                      null);
                }
              }
            });
          }
        });

    eventBus.addHandler(OutputFileUnmap.Event.getType(),
        new OutputFileUnmap.Handler() {
          @Override
          public void onUnmap(OutputFileUnmap.Event event) {
            final List<FilesTreeModel> selection = filesTree.getSelectionModel().getSelection();
            if (selection.isEmpty()) {
              MessageBox.alert("Warning", "Please, select at least one file or field", null);
              return;
            }

            List<Long> idsToUnmap = extractIds(selection);

            switch (selection.get(0).getType()) {
              case FILE:
                fileManager.unmap(idsToUnmap, new GenericAsyncCallback<Void>() {
                  @Override
                  public void onSuccess(Void result) {
                    for (FilesTreeModel selected : selection) {
                      ((OutputFileModel) selected).setParentFile(null);
                      store.update(selected);
                    }
                  }
                });
                break;
              case FIELD:
                fieldManager.unmap(idsToUnmap, new GenericAsyncCallback<Void>() {
                  @Override
                  public void onSuccess(Void result) {
                    for (FilesTreeModel selected : selection) {
                      ((OutputFieldModel) selected).setRule(null);
                      store.update(selected);
                    }
                  }
                });
                break;
              default:
                throw new FilesTreeModel.UnsupportedFilesTreeModelType();
            }
          }
        });

    eventBus.addHandler(OutputFileDelete.Event.getType(),
        new OutputFileDelete.Handler() {
          @Override
          public void onDelete(OutputFileDelete.Event event) {
            final List<FilesTreeModel> selection = filesTree.getSelectionModel().getSelection();
            if (selection.isEmpty()) {
              MessageBox.alert("Warning", "Please, select at least one file or field", null);
              return;
            }

            MessageBox.confirm("Delete", "Do you really want to delete the item?",
                new Listener<MessageBoxEvent>() {
                  @Override
                  public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getText().equals("Yes")) {

                      List<Long> idsToDelete = extractIds(selection);

                      switch (selection.get(0).getType()) {
                        case FILE:
                          fileManager.delete(idsToDelete, new GenericAsyncCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                              Info.display("Success", "The files have been deleted");

                              for (FilesTreeModel selected : selection) {
                                store.remove(selected);
                              }

                              updateWarningState();
                              eventBus.fireEvent(new OutputFileRefresh.Event());
                            }
                          });
                          break;
                        case FIELD:
                          fieldManager.delete(idsToDelete, new GenericAsyncCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                              Info.display("Success", "The fields have been deleted");

                              for (FilesTreeModel selected : selection) {
                                store.remove(selected);
                              }

                              updateWarningState();
                            }
                          });
                          break;
                        default:
                          throw new FilesTreeModel.UnsupportedFilesTreeModelType();
                      }
                    }
                  }
                });
          }
        });

    eventBus.addHandler(OutputFieldAdd.Event.getType(),
        new OutputFieldAdd.Handler() {
          @Override
          public void onAdd(OutputFieldAdd.Event event) {
            List<FilesTreeModel> selection = filesTree.getSelectionModel().getSelection();
            if (!(selection.size() == 1
                && selection.get(0).getType() == FilesTreeModel.Type.FILE)) {
              MessageBox.alert("Warning", "Please, select exactly one file", null);
              return;
            }
            final OutputFileModel file = (OutputFileModel) selection.get(0);

            OutputFieldModel model = event.getModel();
            model.setFile(file.getId());

            fieldManager.save(model, new GenericAsyncCallback<OutputFieldModel>() {
              @Override
              public void onSuccess(OutputFieldModel result) {
                Info.display("Success", "The field has been added");

                store.add(file, result, false);

                updateWarningState();
              }
            });
          }
        });

    eventBus.addHandler(OutputFieldSave.Event.getType(),
        new OutputFieldSave.Handler() {
          @Override
          public void onSave(OutputFieldSave.Event event) {
            OutputFieldModel model = event.getModel();
            fieldManager.save(model, new GenericAsyncCallback<OutputFieldModel>() {
              @Override
              public void onSuccess(OutputFieldModel result) {
                Info.display("Success", "The field has been saved");

                store.update(result);

                updateWarningState();
              }
            });
          }
        });

    eventBus.addHandler(OutputFileReorder.Event.getType(),
        new OutputFileReorder.Handler() {
          @Override
          public void onReorder(OutputFileReorder.Event event) {
            List<Long> fileIds = extractIds(store.getRootItems());
            fileManager.reorder(fileIds, new GenericAsyncCallback<Void>());
          }
        });

    eventBus.addHandler(OutputFieldReorder.Event.getType(),
        new OutputFieldReorder.Handler() {
          @Override
          public void onReorder(OutputFieldReorder.Event event) {
            List<FilesTreeModel> fields = store.getChildren(event.getParent());
            List<Long> fieldIds = extractIds(fields);
            fieldManager.reorder(fieldIds, new GenericAsyncCallback<Void>());
          }
        });

    eventBus.addHandler(OutputFileExpandAll.Event.getType(),
        new OutputFileExpandAll.Handler() {
          @Override
          public void onExpandAll(OutputFileExpandAll.Event event) {
            filesTree.expandAll();
          }
        });

    eventBus.addHandler(OutputFileCollapseAll.Event.getType(),
        new OutputFileCollapseAll.Handler() {
          @Override
          public void onCollapseAll(OutputFileCollapseAll.Event event) {
            filesTree.collapseAll();
          }
        });

    eventBus.addHandler(OutputFileRefresh.Event.getType(),
        new OutputFileRefresh.Handler() {
          @Override
          public void onRefresh(OutputFileRefresh.Event event) {
            List<FilesTreeModel> visibleFiles = store.getRootItems();
            List<Long> visibleFileIds = extractIds(visibleFiles);

            fileManager.refresh(visibleFileIds,
                new GenericAsyncCallback<List<OutputFileModel>>() {
                  @Override
                  public void onSuccess(List<OutputFileModel> files) {
                    for (OutputFileModel file : files) {
                      store.update(file);
                    }
                  }
                });
          }
        });

    eventBus.addHandler(OutputFieldRefresh.Event.getType(),
        new OutputFieldRefresh.Handler() {
          @Override
          public void onRefresh(OutputFieldRefresh.Event event) {
            List<FilesTreeModel> visibleFields = Lists.newArrayList(Iterables.filter(
                store.getAllItems(), new Predicate<FilesTreeModel>() {
                  @Override
                  public boolean apply(FilesTreeModel model) {
                    return model.getType() == FilesTreeModel.Type.FIELD;
                  }
                }));
            List<Long> visibleFieldIds = extractIds(visibleFields);

            fieldManager.refresh(visibleFieldIds,
                new GenericAsyncCallback<List<OutputFieldModel>>() {
                  @Override
                  public void onSuccess(List<OutputFieldModel> fields) {
                    for (OutputFieldModel field : fields) {
                      store.update(field);
                    }
                  }
                });
          }
        });
  }

  private void updateWarningState() {
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        warningTracker.updateState();
      }
    });
  }

  private List<Long> extractIds(List<? extends FilesTreeModel> models) {
    return Lists.newArrayList(Lists.transform(models, new Function<FilesTreeModel, Long>() {
      @Override
      public Long apply(FilesTreeModel model) {
        return model.getId();
      }
    }));
  }

}

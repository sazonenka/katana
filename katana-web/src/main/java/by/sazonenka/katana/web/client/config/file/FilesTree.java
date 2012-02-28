package by.sazonenka.katana.web.client.config.file;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import by.sazonenka.katana.web.client.resources.Icons;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;

import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class FilesTree extends TreePanel<FilesTreeModel> {

  private final Icons icons;

  @Inject
  public FilesTree(TreeStore<FilesTreeModel> store, Icons icons) {
    super(Preconditions.checkNotNull(store));
    this.icons = Preconditions.checkNotNull(icons);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);

    setStateful(true);
    setId("statefullasynctreepanel");
    setTrackMouseOver(false);

    setLabelProvider(createLabelProvider());
    setIconProvider(createIconProvider());
    setSelectionModel(new FilesTreeSelectionModel());
  }

  private ModelStringProvider<FilesTreeModel> createLabelProvider() {
    return new ModelStringProvider<FilesTreeModel>() {
      @Override
      public String getStringValue(FilesTreeModel model, String property) {
        switch (model.getType()) {
          case FILE:
            OutputFileModel file = (OutputFileModel) model;
            if (file.getParentFile() != null) {
              return file.getName() + " (extends " + file.getParentFile() + ")";
            } else {
              return file.getName();
            }
          case FIELD:
            OutputFieldModel field = (OutputFieldModel) model;
            if (field.getRule() != null) {
              return field.getName() + " (mapped to " + field.getRule() + ")";
            } else {
              return field.getName();
            }
          default:
            throw new FilesTreeModel.UnsupportedFilesTreeModelType();
        }
      }
    };
  }

  private ModelIconProvider<FilesTreeModel> createIconProvider() {
    return new ModelIconProvider<FilesTreeModel>() {
      @Override
      public AbstractImagePrototype getIcon(FilesTreeModel model) {
        switch (model.getType()) {
          case FILE:
            return icons.outputFile();
          case FIELD:
            return icons.outputField();
          default:
            throw new FilesTreeModel.UnsupportedFilesTreeModelType();
        }
      }
    };
  }

  private static final class FilesTreeSelectionModel extends TreePanelSelectionModel<FilesTreeModel> {

    FilesTreeSelectionModel() {
      addListener(Events.BeforeSelect, createBeforeSelectListener());
    }

    private Listener<SelectionEvent<FilesTreeModel>> createBeforeSelectListener() {
      return new Listener<SelectionEvent<FilesTreeModel>>() {
        @Override
        public void handleEvent(SelectionEvent<FilesTreeModel> se) {
          FilesTreeModel newSelected = se.getModel();
          for (FilesTreeModel alreadySelected : getSelectedItems()) {
            if (!isSelectionValid(alreadySelected, newSelected)) {
              se.setCancelled(true);
            }
          }
        }

        private boolean isSelectionValid(FilesTreeModel alreadySelected, FilesTreeModel newSelected) {
          FilesTreeModel.Type newSelectedType = newSelected.getType();
          FilesTreeModel.Type selectedType = alreadySelected.getType();
          if (newSelectedType != selectedType) {
            return false;
          }

          switch (selectedType) {
            case FILE:
              break;
            case FIELD:
              OutputFieldModel newSelectedField = (OutputFieldModel) newSelected;
              OutputFieldModel alreadySelectedField = (OutputFieldModel) alreadySelected;
              if (!newSelectedField.getFile().equals(alreadySelectedField.getFile())) {
                return false;
              }
              break;
            default:
              throw new FilesTreeModel.UnsupportedFilesTreeModelType();
          }
          return true;
        }
      };
    }

    @Override
    public List<FilesTreeModel> getSelectedItems() {
      List<FilesTreeModel> selectedItems = super.getSelectedItems();
      Collections.sort(selectedItems, new Comparator<FilesTreeModel>() {
        @Override
        public int compare(FilesTreeModel m1, FilesTreeModel m2) {
          int i1 = treeStore.indexOf(m1);
          int i2 = treeStore.indexOf(m2);
          return i1 - i2;
        }
      });
      return selectedItems;
    }
  }

  @Override
  public TreeNode findNode(FilesTreeModel m) {
    return super.findNode(m);
  }

}

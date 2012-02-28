package by.sazonenka.katana.web.client.config.dnd;

import java.util.List;

import by.sazonenka.katana.web.client.config.file.FilesTree;
import by.sazonenka.katana.web.client.events.OutputFieldReorder;
import by.sazonenka.katana.web.client.events.OutputFileMapToParentFile;
import by.sazonenka.katana.web.client.events.OutputFileReorder;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.dnd.Insert;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.TreeStoreModel;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;

/**
 * @author Aliaksandr Sazonenka
 */
@SuppressWarnings("rawtypes")
public final class FilesTreeDropTarget extends TreePanelDropTarget {

  private final EventBus eventBus;

  public FilesTreeDropTarget(FilesTree filesTree, EventBus eventBus) {
    super(filesTree);
    this.eventBus = Preconditions.checkNotNull(eventBus);

    setAllowSelfAsSource(true);
    setAllowDropOnLeaf(true);
    setFeedback(Feedback.INSERT);
  }

  @Override
  public FilesTree getTree() {
    return (FilesTree) super.getTree();
  }

  @Override
  protected void showFeedback(DNDEvent event) {
    FilesTree filesTree = getTree();

    if (event.isAltKey()) {
      feedback = Feedback.APPEND;
      operation = Operation.COPY;
    } else {
      feedback = Feedback.INSERT;
      operation = Operation.MOVE;
    }

    List<FilesTreeModel> selection = filesTree.getSelectionModel().getSelection();
    FilesTreeModel sourceModel = selection.get(0);

    TreeNode overNode = filesTree.findNode(event.getTarget());
    if (overNode != null) {
      FilesTreeModel targetModel = (FilesTreeModel) overNode.getModel();

      if (isDNDValid(sourceModel, targetModel)) {
        super.showFeedback(event);
      } else {
        clearStyles(event);
      }
    } else {
      if (feedback == Feedback.INSERT) {
        if (sourceModel.getType() == FilesTreeModel.Type.FILE) {
          int sourceModelIndex = filesTree.getStore().indexOf(sourceModel);
          if (sourceModelIndex < filesTree.getStore().getChildCount() - 1) {
            event.getStatus().setStatus(true, "x-tree-drop-ok-below");

            status = 1;
            activeItem = filesTree.findNode(getLastVisibleModel());
            showInsert(activeItem.getElement(), false);
          }
        }
      } else {
        clearStyles(event);
      }
    }
  }

  @Override
  protected void handleAppend(DNDEvent event, TreeNode item) {
    FilesTreeModel targetModel = (FilesTreeModel) item.getModel();
    if (targetModel.getType() == FilesTreeModel.Type.FILE) {
      super.handleAppend(event, item);
    }
  }

  private boolean isDNDValid(FilesTreeModel sourceModel, FilesTreeModel targetModel) {
    FilesTreeModel.Type sourceModelType = sourceModel.getType();
    FilesTreeModel.Type targetModelType = targetModel.getType();
    if (sourceModelType != targetModelType) {
      return false;
    }

    switch (sourceModelType) {
      case FILE:
        return true;
      case FIELD:
        OutputFieldModel sourceField = (OutputFieldModel) sourceModel;
        OutputFieldModel targetField = (OutputFieldModel) targetModel;
        return targetField.getFile().equals(sourceField.getFile());
      default:
        throw new FilesTreeModel.UnsupportedFilesTreeModelType();
    }
  }

  private FilesTreeModel getLastVisibleModel() {
    FilesTree filesTree = getTree();

    FilesTreeModel parent = null;
    do {
      parent = filesTree.getStore().getLastChild(parent);
    } while (!filesTree.isLeaf(parent) && filesTree.isExpanded(parent));
    return parent;
  }

  private void showInsert(Element elem, boolean before) {
    Insert insert = Insert.get();
    insert.show(elem);
    Rectangle rect = El.fly(elem).getBounds();
    int y = before ? rect.y - 2 : (rect.y + rect.height - 4);
    insert.setBounds(rect.x, y, rect.width, 6);
  }

  @Override
  protected void handleInsertDrop(DNDEvent event, TreeNode item, int index) {
    TreeNode targetForInsert = item;

    List<TreeStoreModel> selection = event.getData();
    if (!selection.isEmpty()) {
      FilesTreeModel sourceModel = (FilesTreeModel) selection.get(0).getModel();
      FilesTreeModel lastVisibleModel = getLastVisibleModel();

      if (sourceModel.getType() != lastVisibleModel.getType()
          && item.getModel().equals(lastVisibleModel)) {
        targetForInsert = item.getParent();
      }

      super.handleInsertDrop(event, targetForInsert, index);

      switch (sourceModel.getType()) {
        case FILE:
          eventBus.fireEvent(new OutputFileReorder.Event());
          break;
        case FIELD:
          TreeNode sourceModelNode = getTree().findNode(sourceModel);
          OutputFileModel parent = (OutputFileModel) sourceModelNode.getParent().getModel();
          eventBus.fireEvent(new OutputFieldReorder.Event(parent));
          break;
        default:
          throw new FilesTreeModel.UnsupportedFilesTreeModelType();
      }
    }
  }

  @Override
  protected void handleAppendDrop(DNDEvent event, TreeNode item) {
    Object data = event.getData();
    List<ModelData> models = prepareDropData(data, true);
    if (!models.isEmpty()) {
      List<OutputFileModel> files = Lists.newArrayList();
      for (ModelData model : models) {
        files.add((OutputFileModel) model);
      }

      OutputFileModel parent = (OutputFileModel) item.getModel();
      eventBus.fireEvent(new OutputFileMapToParentFile.Event(files, parent));
    }
  }

}

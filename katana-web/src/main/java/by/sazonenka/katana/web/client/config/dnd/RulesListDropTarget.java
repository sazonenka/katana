package by.sazonenka.katana.web.client.config.dnd;

import java.util.List;

import by.sazonenka.katana.web.client.config.rule.RulesList;
import by.sazonenka.katana.web.client.events.OutputFieldMapToRule;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.TreeStoreModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;

/**
 * @author Aliaksandr Sazonenka
 */
public final class RulesListDropTarget extends ListViewDropTarget {

  private final EventBus eventBus;

  public RulesListDropTarget(RulesList rulesList, EventBus eventBus) {
    super(rulesList);
    this.eventBus = Preconditions.checkNotNull(eventBus);

    setOperation(Operation.COPY);
  }

  @Override
  protected void showFeedback(DNDEvent event) {
    if (event.isAltKey()) {
      feedback = Feedback.APPEND;
      operation = Operation.COPY;
    } else {
      feedback = Feedback.INSERT;
      operation = Operation.MOVE;
    }

    Element row = listView.findElement(event.getTarget()).cast();
    if (feedback == Feedback.APPEND && row != null) {
      List<TreeStoreModel> selection = event.getData();
      if (!selection.isEmpty()) {
        FilesTreeModel sourceModel = (FilesTreeModel) selection.get(0).getModel();
        if (sourceModel.getType() == FilesTreeModel.Type.FIELD) {
          super.showFeedback(event);
          return;
        }
      }
    }
    event.setCancelled(true);
    event.getStatus().setStatus(false);
  }

  @Override
  protected void onDragDrop(DNDEvent event) {
    Object data = event.getData();
    List<ModelData> models = prepareDropData(data, true);
    if (!models.isEmpty()) {
      List<OutputFieldModel> fields = Lists.newArrayList();
      for (ModelData model : models) {
        fields.add((OutputFieldModel) model);
      }

      int targetElementIndex = listView.findElementIndex(event.getTarget());
      ModelData ruleModel = listView.getStore().getAt(targetElementIndex);
      ValidationRuleModel rule = (ValidationRuleModel) ruleModel;

      eventBus.fireEvent(new OutputFieldMapToRule.Event(fields, rule));
    }
  }

}

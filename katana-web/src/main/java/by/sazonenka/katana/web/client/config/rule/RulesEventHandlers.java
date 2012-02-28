package by.sazonenka.katana.web.client.config.rule;

import java.util.List;
import java.util.Map;

import by.sazonenka.katana.web.client.config.ConfigDictionary;
import by.sazonenka.katana.web.client.config.ConfigWarningTracker;
import by.sazonenka.katana.web.client.events.OutputFieldRefresh;
import by.sazonenka.katana.web.client.events.ValidationRuleCollapseAll;
import by.sazonenka.katana.web.client.events.ValidationRuleDelete;
import by.sazonenka.katana.web.client.events.ValidationRuleExpandAll;
import by.sazonenka.katana.web.client.events.ValidationRuleLoad;
import by.sazonenka.katana.web.client.events.ValidationRuleMoveDown;
import by.sazonenka.katana.web.client.events.ValidationRuleMoveUp;
import by.sazonenka.katana.web.client.events.ValidationRuleSave;
import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.ValidationRuleManagerAsync;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.common.base.Preconditions;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class RulesEventHandlers {

  private final ListStore<ValidationRuleModel> store;
  private final Map<ValidationRuleModel, RuleForm> ruleFormStore;
  private final RuleFormBuilder ruleFormBuilder;

  private final EventBus eventBus;
  private final ConfigDictionary dictionary;
  private final ConfigWarningTracker warningTracker;

  private final ValidationRuleManagerAsync ruleManager;

  @Inject
  public RulesEventHandlers(ListStore<ValidationRuleModel> store,
      Map<ValidationRuleModel, RuleForm> ruleFormStore,
      RuleFormBuilder ruleFormBuilder,
      EventBus eventBus,
      ConfigDictionary dictionary,
      ConfigWarningTracker warningTracker,
      ValidationRuleManagerAsync ruleManager) {
    this.store = Preconditions.checkNotNull(store);
    this.ruleFormStore = Preconditions.checkNotNull(ruleFormStore);
    this.ruleFormBuilder = Preconditions.checkNotNull(ruleFormBuilder);

    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.dictionary = Preconditions.checkNotNull(dictionary);
    this.warningTracker = Preconditions.checkNotNull(warningTracker);

    this.ruleManager = Preconditions.checkNotNull(ruleManager);
  }

  public void attach(final ContentPanel rulesTab) {
    eventBus.addHandler(ValidationRuleLoad.Event.getType(),
        new ValidationRuleLoad.Handler() {
          @Override
          public void onLoad(ValidationRuleLoad.Event event) {
            ruleManager.findByConfig(dictionary.getConfigId(),
                new GenericAsyncCallback<List<ValidationRuleModel>>() {
                  @Override
                  public void onSuccess(List<ValidationRuleModel> result) {
                    for (ValidationRuleModel rule : result) {
                      RuleForm ruleForm = ruleFormBuilder.build(rule);
                      rulesTab.add(ruleForm, new RowData(1, 1, new Margins(4)));

                      store.add(rule);
                      ruleFormStore.put(rule, ruleForm);
                    }
                    updateUpDownButtonsVisibility();
                    updateWarningState();
                    rulesTab.layout();
                  }
                });
          }
        });

    eventBus.addHandler(ValidationRuleSave.Event.getType(),
        new ValidationRuleSave.Handler() {
          @Override
          public void onSave(ValidationRuleSave.Event event) {
            ValidationRuleModel model = event.getModel();
            model.setConfig(dictionary.getConfigId());

            ruleManager.save(model, new GenericAsyncCallback<ValidationRuleModel>() {
              @Override
              public void onSuccess(ValidationRuleModel result) {
                Info.display("Success", "The rule has been saved");

                if (!store.contains(result)) {
                  RuleForm ruleForm = ruleFormBuilder.build(result);
                  rulesTab.add(ruleForm, new RowData(1, 1, new Margins(4)));
                  ruleForm.expand();

                  store.add(result);
                  ruleFormStore.put(result, ruleForm);
                } else {
                  store.update(result);

                  eventBus.fireEvent(new OutputFieldRefresh.Event());
                }

                updateUpDownButtonsVisibility();
                updateWarningState();
                rulesTab.layout();
              }
            });
          }
        });

    eventBus.addHandler(ValidationRuleMoveUp.Event.getType(),
        new ValidationRuleMoveUp.Handler() {
          @Override
          public void onMoveUp(ValidationRuleMoveUp.Event event) {
            final ValidationRuleModel rule1 = event.getModel();
            final ValidationRuleModel rule2 = store.getAt(store.indexOf(rule1) - 1);

            ruleManager.swap(rule1.getId(), rule2.getId(), new GenericAsyncCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                Info.display("Success", "The rule has been moved up");

                store.remove(rule1);
                store.insert(rule1, store.indexOf(rule2));

                RuleForm rule1Form = ruleFormStore.get(rule1);
                RuleForm rule2Form = ruleFormStore.get(rule2);

                rulesTab.remove(rule1Form);
                rulesTab.insert(rule1Form, rulesTab.indexOf(rule2Form));

                updateUpDownButtonsVisibility();
                rulesTab.layout();
              }
            });
          }
        });

    eventBus.addHandler(ValidationRuleMoveDown.Event.getType(),
        new ValidationRuleMoveDown.Handler() {
          @Override
          public void onMoveDown(ValidationRuleMoveDown.Event event) {
            final ValidationRuleModel rule2 = event.getModel();
            final ValidationRuleModel rule1 = store.getAt(store.indexOf(rule2) + 1);

            ruleManager.swap(rule1.getId(), rule2.getId(), new GenericAsyncCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                Info.display("Success", "The rule has been moved down");

                store.remove(rule1);
                store.insert(rule1, store.indexOf(rule2));

                RuleForm rule1Form = ruleFormStore.get(rule1);
                RuleForm rule2Form = ruleFormStore.get(rule2);

                rulesTab.remove(rule1Form);
                rulesTab.insert(rule1Form, rulesTab.indexOf(rule2Form));

                updateUpDownButtonsVisibility();
                rulesTab.layout();
              }
            });
          }
        });

    eventBus.addHandler(ValidationRuleDelete.Event.getType(),
        new ValidationRuleDelete.Handler() {
          @Override
          public void onDelete(final ValidationRuleDelete.Event event) {
            MessageBox.confirm("Delete", "Do you really want to delete the rule?",
                new Listener<MessageBoxEvent>() {
                  @Override
                  public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getText().equals("Yes")) {
                      final ValidationRuleModel model = event.getModel();

                      ruleManager.delete(model.getId(), new GenericAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                          Info.display("Success", "The rule has been deleted");

                          ruleFormStore.get(model).removeFromParent();

                          store.remove(model);
                          ruleFormStore.remove(model);

                          updateUpDownButtonsVisibility();
                          updateWarningState();

                          eventBus.fireEvent(new OutputFieldRefresh.Event());
                        }
                      });
                    }
                  }
                });
          }
        });

    eventBus.addHandler(ValidationRuleExpandAll.Event.getType(),
        new ValidationRuleExpandAll.Handler() {
          @Override
          public void onExpandAll(ValidationRuleExpandAll.Event event) {
            for (RuleForm ruleForm : ruleFormStore.values()) {
              ruleForm.expand();
            }
          }
        });

    eventBus.addHandler(ValidationRuleCollapseAll.Event.getType(),
        new ValidationRuleCollapseAll.Handler() {
          @Override
          public void onCollapseAll(ValidationRuleCollapseAll.Event event) {
            for (RuleForm ruleForm : ruleFormStore.values()) {
              ruleForm.collapse();
            }
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

  private void updateUpDownButtonsVisibility() {
    for (ValidationRuleModel rule : ruleFormStore.keySet()) {
      RuleForm ruleForm = ruleFormStore.get(rule);
      ruleForm.updateUpDownButtonsVisibility(store.indexOf(rule), store.getCount());
    }
  }

}

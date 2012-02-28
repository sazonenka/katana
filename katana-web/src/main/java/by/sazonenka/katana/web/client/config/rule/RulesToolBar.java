package by.sazonenka.katana.web.client.config.rule;

import by.sazonenka.katana.web.client.config.ConfigWarningTracker;
import by.sazonenka.katana.web.client.events.ValidationRuleCollapseAll;
import by.sazonenka.katana.web.client.events.ValidationRuleExpandAll;
import by.sazonenka.katana.web.client.events.ValidationRuleSave;
import by.sazonenka.katana.web.client.resources.Icons;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class RulesToolBar extends ToolBar {

  private final EventBus eventBus;
  private final Icons icons;
  private final ConfigWarningTracker warningTracker;

  @Inject
  public RulesToolBar(EventBus eventBus, Icons icons, ConfigWarningTracker warningTracker) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.icons = Preconditions.checkNotNull(icons);
    this.warningTracker = Preconditions.checkNotNull(warningTracker);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);

    createButtons();
    createWarningIndicator();
  }

  private void createButtons() {
    Button addButton = new Button("Add Validation Rule", icons.addItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            ValidationRuleModel model = new ValidationRuleModel();
            model.setName("rule_name");
            model.setNullable(false);
            model.setRegexp("rule_regexp");
            model.setDescription("rule_description");

            eventBus.fireEvent(new ValidationRuleSave.Event(model));
          }
        });
    add(addButton);

    add(new SeparatorToolItem());

    Button expandAllButton = new Button("Expand All", icons.expandAllItems(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new ValidationRuleExpandAll.Event());
          }
        });
    add(expandAllButton);

    Button collapseAllButton = new Button("Collapse All", icons.collapseAllItems(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new ValidationRuleCollapseAll.Event());
          }
        });
    add(collapseAllButton);
  }

  private void createWarningIndicator() {
    El row = getPositionEl().child("tr.x-toolbar-right-row");
    warningTracker.register(row);
  }

}

package by.sazonenka.katana.web.client.config.rule;

import by.sazonenka.katana.web.client.events.ValidationRuleDelete;
import by.sazonenka.katana.web.client.events.ValidationRuleMoveDown;
import by.sazonenka.katana.web.client.events.ValidationRuleMoveUp;
import by.sazonenka.katana.web.client.events.ValidationRuleSave;
import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;

/**
 * @author Aliaksandr Sazonenka
 */
public final class RuleForm extends FormPanel {

  private final EventBus eventBus;
  private final ValidationRuleModel model;

  private final TextField<String> ruleNameField = new TextField<String>();
  private final CheckBox ruleNullableField = new CheckBox();
  private final TextField<String> ruleRegexpField = new TextField<String>();
  private final TextArea ruleDescriptionField = new TextArea();

  private final ToolButton upButton = new ToolButton("x-tool-up");
  private final ToolButton downButton = new ToolButton("x-tool-down");

  public RuleForm(EventBus eventBus, ValidationRuleModel model) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.model = Preconditions.checkNotNull(model);

    setRenderSettings();
    addFields();
    addTools();
  }

  private void setRenderSettings() {
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(200);
    layout.setDefaultWidth(650);
    setLayout(layout);

    setHeight(185);

    setCollapsible(true);
    setTitleCollapse(true);
    setAnimCollapse(false);
    collapse();
  }

  private void addFields() {
    setHeading(model.getName());

    ruleNameField.setName(ValidationRuleModel.Attributes.NAME.id());
    ruleNameField.setFieldLabel(ValidationRuleModel.Attributes.NAME.title());
    ruleNameField.setAllowBlank(false);
    ruleNameField.setValue(model.getName());
    add(ruleNameField);

    CheckBoxGroup ruleNullableCheckBoxGroup = new CheckBoxGroup();
    ruleNullableCheckBoxGroup.setName(ValidationRuleModel.Attributes.NULLABLE.id());
    ruleNullableCheckBoxGroup.setFieldLabel(ValidationRuleModel.Attributes.NULLABLE.title());
    ruleNullableField.setValue(model.getNullable());
    ruleNullableCheckBoxGroup.add(ruleNullableField);
    add(ruleNullableCheckBoxGroup);

    ruleRegexpField.setName(ValidationRuleModel.Attributes.REGEXP.id());
    ruleRegexpField.setFieldLabel(ValidationRuleModel.Attributes.REGEXP.title());
    ruleRegexpField.setAllowBlank(false);
    ruleRegexpField.setValue(model.getRegexp());
    add(ruleRegexpField);

    ruleDescriptionField.setName(ValidationRuleModel.Attributes.DESCRIPTION.id());
    ruleDescriptionField.setFieldLabel(ValidationRuleModel.Attributes.DESCRIPTION.title());
    ruleDescriptionField.setAllowBlank(true);
    ruleDescriptionField.setValue(model.getDescription());
    add(ruleDescriptionField);
  }

  private void addTools() {
    Header header = getHeader();

    upButton.addSelectionListener(new SelectionListener<IconButtonEvent>() {
      @Override
      public void componentSelected(IconButtonEvent ibe) {
        eventBus.fireEvent(new ValidationRuleMoveUp.Event(model));
      }
    });
    upButton.hide();
    header.insertTool(upButton, header.getToolCount());

    downButton.addSelectionListener(new SelectionListener<IconButtonEvent>() {
      @Override
      public void componentSelected(IconButtonEvent ibe) {
        eventBus.fireEvent(new ValidationRuleMoveDown.Event(model));
      }
    });
    downButton.hide();
    header.insertTool(downButton, header.getToolCount());

    ToolButton saveButton = new ToolButton("x-tool-save",
        new SelectionListener<IconButtonEvent>() {
          @Override
          public void componentSelected(IconButtonEvent ibe) {
            if (isValid()) {
              setHeading(ruleNameField.getValue());

              model.setName(ruleNameField.getValue());
              model.setNullable(ruleNullableField.getValue());
              model.setRegexp(ruleRegexpField.getValue());
              model.setDescription(ruleDescriptionField.getValue());

              eventBus.fireEvent(new ValidationRuleSave.Event(model));
            } else {
              MessageBox.alert("Error", "The entered data is incorrect", null);
            }
          }
        });
    header.insertTool(saveButton, header.getToolCount());

    ToolButton deleteButton = new ToolButton("x-tool-close",
        new SelectionListener<IconButtonEvent>() {
          @Override
          public void componentSelected(IconButtonEvent ibe) {
            eventBus.fireEvent(new ValidationRuleDelete.Event(model));
          }
        });
    header.insertTool(deleteButton, header.getToolCount());
  }

  public void updateUpDownButtonsVisibility(int indexInList, int listSize) {
    boolean upButtonVisible = indexInList > 0 ? true : false;
    upButton.setVisible(upButtonVisible);

    boolean downButtonVisible = indexInList < listSize - 1 ? true : false;
    downButton.setVisible(downButtonVisible);
  }

}

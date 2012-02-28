package by.sazonenka.katana.web.client.directory;

import by.sazonenka.katana.web.client.events.ConstraintConfigSave;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConfigEditorPopup extends Window {

  private final EventBus eventBus;
  private final ConstraintConfigModel model;

  private final FormPanel form = new FormPanel();
  private final TextField<String> configNameField = new TextField<String>();
  private final TextField<String> configAuthorField = new TextField<String>();

  public ConfigEditorPopup(EventBus eventBus) {
    this(eventBus, new ConstraintConfigModel());
  }

  public ConfigEditorPopup(EventBus eventBus, ConstraintConfigModel model) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.model = Preconditions.checkNotNull(model);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    setSize(500, 150);
    setResizable(false);
    setPlain(true);
    setModal(true);
    setHeading("Constraint Config");
    setLayout(new FitLayout());

    add(createForm());

    addListener(Events.Focus, new Listener<BaseEvent>() {
      @Override
      public void handleEvent(BaseEvent be) {
        configNameField.focus();
      }
    });
  }

  private FormPanel createForm() {
    form.setFrame(true);
    form.setHeaderVisible(false);

    FormLayout layout = new FormLayout();
    layout.setLabelWidth(100);
    layout.setDefaultWidth(330);
    form.setLayout(layout);

    configNameField.setName(ConstraintConfigModel.Attributes.NAME.id());
    configNameField.setFieldLabel(ConstraintConfigModel.Attributes.NAME.title());
    configNameField.setAllowBlank(false);
    form.add(configNameField);

    configAuthorField.setName(ConstraintConfigModel.Attributes.AUTHOR.id());
    configAuthorField.setFieldLabel(ConstraintConfigModel.Attributes.AUTHOR.title());
    configAuthorField.setAllowBlank(true);
    form.add(configAuthorField);

    configNameField.setValue(model.getName());
    configAuthorField.setValue(model.getAuthor());

    form.setButtonAlign(HorizontalAlignment.CENTER);
    form.addButton(new Button("Save", new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent be) {
        submit();
      }
    }));
    form.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent be) {
        hide();
      }
    }));

    new KeyNav<ComponentEvent>() {
      @Override
      public void onEnter(ComponentEvent ce) {
        submit();
      }
    }.bind(this);

    return form;
  }

  private void submit() {
    if (form.isValid()) {
      model.setName(configNameField.getValue());
      model.setAuthor(configAuthorField.getValue());

      eventBus.fireEvent(new ConstraintConfigSave.Event(model));
      hide();
    } else {
      MessageBox.alert("Error", "The entered data is incorrect", null);
    }
  }

}

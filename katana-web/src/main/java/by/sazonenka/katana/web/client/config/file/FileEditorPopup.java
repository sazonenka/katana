package by.sazonenka.katana.web.client.config.file;

import by.sazonenka.katana.web.client.events.OutputFieldSave;
import by.sazonenka.katana.web.client.events.OutputFileSave;
import by.sazonenka.katana.web.model.ConstraintConfigModel;
import by.sazonenka.katana.web.model.FilesTreeModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;

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
public final class FileEditorPopup extends Window {

  private final EventBus eventBus;
  private final FilesTreeModel model;

  private final FormPanel form = new FormPanel();
  private final TextField<String> itemNameField = new TextField<String>();

  public FileEditorPopup(EventBus eventBus, FilesTreeModel model) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.model = Preconditions.checkNotNull(model);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    setSize(500, 130);
    setResizable(false);
    setPlain(true);
    setModal(true);
    setLayout(new FitLayout());

    switch (model.getType()) {
      case FILE:
        setHeading("Output File");
        break;
      case FIELD:
        setHeading("Output Field");
        break;
      default:
        throw new FilesTreeModel.UnsupportedFilesTreeModelType();
    }

    add(createForm());

    addListener(Events.Focus, new Listener<BaseEvent>() {
      @Override
      public void handleEvent(BaseEvent be) {
        itemNameField.focus();
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

    itemNameField.setName(ConstraintConfigModel.Attributes.NAME.id());
    itemNameField.setFieldLabel(ConstraintConfigModel.Attributes.NAME.title());
    itemNameField.setAllowBlank(false);
    form.add(itemNameField);

    itemNameField.setValue(model.getName());

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
      model.setName(itemNameField.getValue());

      switch (model.getType()) {
        case FILE:
          eventBus.fireEvent(new OutputFileSave.Event((OutputFileModel) model));
          break;
        case FIELD:
          eventBus.fireEvent(new OutputFieldSave.Event((OutputFieldModel) model));
          break;
        default:
          throw new FilesTreeModel.UnsupportedFilesTreeModelType();
      }
      hide();
    } else {
      MessageBox.alert("Error", "The entered data is incorrect", null);
    }
  }

}

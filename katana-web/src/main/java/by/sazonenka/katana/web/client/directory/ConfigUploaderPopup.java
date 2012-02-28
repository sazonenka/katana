package by.sazonenka.katana.web.client.directory;

import by.sazonenka.katana.web.client.events.ConstraintConfigUpload;
import by.sazonenka.katana.web.model.upload.UploadStatus.Status;
import by.sazonenka.katana.web.model.upload.UploadStatusJso;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConfigUploaderPopup extends Window {

  private final EventBus eventBus;

  private final FormPanel form = new FormPanel();
  private final FileUploadField fileUploadField = new FileUploadField();

  public ConfigUploaderPopup(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    setSize(500, 150);
    setResizable(false);
    setPlain(true);
    setModal(true);
    setHeading("Upload Constraint Config");
    setLayout(new FitLayout());

    add(createForm());
  }

  private FormPanel createForm() {
    form.setAction("upload.htm");
    form.setEncoding(Encoding.MULTIPART);
    form.setMethod(Method.POST);

    form.setFrame(true);
    form.setHeaderVisible(false);

    FormLayout layout = new FormLayout();
    layout.setLabelWidth(100);
    layout.setDefaultWidth(330);
    form.setLayout(layout);

    fileUploadField.setName("file");
    fileUploadField.setFieldLabel("File");
    fileUploadField.setAllowBlank(false);
    form.add(fileUploadField);

    form.setButtonAlign(HorizontalAlignment.CENTER);
    form.addButton(new Button("Upload", new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent be) {
        if (form.isValid()) {
          form.submit();
        } else {
          MessageBox.alert("Error", "The entered data is incorrect", null);
        }
      }
    }));
    form.addButton(new Button("Cancel", new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent be) {
        hide();
      }
    }));

    form.addListener(Events.Submit, new Listener<FormEvent>() {
      @Override
      public void handleEvent(FormEvent e) {
        UploadStatusJso status = UploadStatusJso.build(e.getResultHtml());

        if (status.getStatus() == Status.SUCCESS) {
          eventBus.fireEvent(new ConstraintConfigUpload.Event());
        } else {
          MessageBox.alert("Error", status.getDescription(), null);
        }
      }
    });

    return form;
  }

}

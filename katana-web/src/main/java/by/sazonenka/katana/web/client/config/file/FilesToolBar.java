package by.sazonenka.katana.web.client.config.file;

import by.sazonenka.katana.web.client.config.ConfigWarningTracker;
import by.sazonenka.katana.web.client.events.OutputFieldAdd;
import by.sazonenka.katana.web.client.events.OutputFileCollapseAll;
import by.sazonenka.katana.web.client.events.OutputFileDelete;
import by.sazonenka.katana.web.client.events.OutputFileExpandAll;
import by.sazonenka.katana.web.client.events.OutputFileRename;
import by.sazonenka.katana.web.client.events.OutputFileSave;
import by.sazonenka.katana.web.client.events.OutputFileUnmap;
import by.sazonenka.katana.web.client.resources.Icons;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;

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
public final class FilesToolBar extends ToolBar {

  private final EventBus eventBus;
  private final Icons icons;
  private final ConfigWarningTracker warningTracker;

  @Inject
  public FilesToolBar(EventBus eventBus, Icons icons, ConfigWarningTracker warningTracker) {
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
    Button addFileButton = new Button("Add File", icons.addItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            OutputFileModel model = new OutputFileModel();
            model.setName("output_file.csv");

            eventBus.fireEvent(new OutputFileSave.Event(model));
          }
        });
    add(addFileButton);

    Button addFieldButton = new Button("Add Field", icons.addItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            OutputFieldModel model = new OutputFieldModel();
            model.setName("output_field");

            eventBus.fireEvent(new OutputFieldAdd.Event(model));
          }
        });
    add(addFieldButton);

    add(new SeparatorToolItem());

    Button renameButton = new Button("Rename", icons.renameItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new OutputFileRename.Event());
          }
        });
    add(renameButton);

    Button unmapButton = new Button("Unmap", icons.unmapItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new OutputFileUnmap.Event());
          }
        });
    add(unmapButton);

    Button deleteButton = new Button("Delete", icons.deleteItem(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new OutputFileDelete.Event());
          }
        });
    add(deleteButton);

    add(new SeparatorToolItem());

    Button expandAllButton = new Button("Expand All", icons.expandAllItems(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new OutputFileExpandAll.Event());
          }
        });
    add(expandAllButton);

    Button collapseAllButton = new Button("Collapse All", icons.collapseAllItems(),
        new SelectionListener<ButtonEvent>() {
          @Override
          public void componentSelected(ButtonEvent be) {
            eventBus.fireEvent(new OutputFileCollapseAll.Event());
          }
        });
    add(collapseAllButton);
  }

  private void createWarningIndicator() {
    El row = getPositionEl().child("tr.x-toolbar-right-row");
    warningTracker.register(row);
  }

}

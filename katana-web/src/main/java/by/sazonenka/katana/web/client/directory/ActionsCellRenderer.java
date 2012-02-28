package by.sazonenka.katana.web.client.directory;

import by.sazonenka.katana.web.client.events.ConstraintConfigDelete;
import by.sazonenka.katana.web.client.events.ConstraintConfigDownload;
import by.sazonenka.katana.web.client.events.ConstraintConfigEdit;
import by.sazonenka.katana.web.client.events.ConstraintConfigFocus;
import by.sazonenka.katana.web.client.events.ConstraintConfigOpen;
import by.sazonenka.katana.web.client.resources.Icons;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class ActionsCellRenderer implements GridCellRenderer<ConstraintConfigModel> {

  private final EventBus eventBus;
  private final Icons icons;

  @Inject
  public ActionsCellRenderer(EventBus eventBus, Icons icons) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.icons = Preconditions.checkNotNull(icons);
  }

  @Override
  public Object render(final ConstraintConfigModel model, String property, ColumnData config,
      int rowIndex, int colIndex, ListStore<ConstraintConfigModel> store,
      Grid<ConstraintConfigModel> grid) {
    final ButtonBar buttonBar = new ButtonBar();

    buttonBar.add(createButton("Open the constraint config",
        icons.openConfig(),
        new ConstraintConfigOpen.Event(model)));
    buttonBar.add(createButton("Download the constraint config",
        icons.downloadConfig(),
        new ConstraintConfigDownload.Event(model)));
    buttonBar.add(createButton("Edit the constraint config",
        icons.editConfig(),
        new ConstraintConfigEdit.Event(model)));
    buttonBar.add(createButton("Delete the constraint config",
        icons.deleteConfig(),
        new ConstraintConfigDelete.Event(model)));

    buttonBar.setHideMode(HideMode.VISIBILITY);
    buttonBar.hide();

    eventBus.addHandler(ConstraintConfigFocus.Event.getType(),
        new ConstraintConfigFocus.Handler() {
          @Override
          public void onFocus(ConstraintConfigFocus.Event event) {
            if (event.getModel().equals(model)) {
              buttonBar.show();
            } else {
              if (buttonBar.isVisible()) {
                buttonBar.hide();
              }
            }
          }
    });

    return buttonBar;
  }

  private Button createButton(String toolTip, AbstractImagePrototype icon,
      final GwtEvent<?> clickEvent) {
    Button button = new Button();

    button.setToolTip(toolTip);
    button.setIcon(icon);
    button.setScale(ButtonScale.SMALL);

    button.addSelectionListener(new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent be) {
        eventBus.fireEvent(clickEvent);
      }
    });

    return button;
  }

}

package by.sazonenka.katana.web.client.directory;

import java.util.ArrayList;
import java.util.List;

import by.sazonenka.katana.web.client.events.ConstraintConfigFocus;
import by.sazonenka.katana.web.client.events.ConstraintConfigLoad;
import by.sazonenka.katana.web.client.resources.Icons;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author Aliaksandr Sazonenka
 */
public final class DirectoryGrid extends LayoutContainer {

  private final ActionsCellRenderer actionsCellRenderer;
  private final ConfigEditorPopupBuilder configEditorPopupBuilder;
  private final ConfigUploaderPopupBuilder configUploaderPopupBuilder;

  private final EventBus eventBus;
  private final DirectoryEventHandlers eventHandlers;

  private final Icons icons;

  @Inject
  public DirectoryGrid(ActionsCellRenderer actionsCellRenderer,
      ConfigEditorPopupBuilder configEditorPopupBuilder,
      ConfigUploaderPopupBuilder configUploaderPopupBuilder,
      EventBus eventBus,
      DirectoryEventHandlers eventHandlers,
      Icons icons) {
    this.actionsCellRenderer = Preconditions.checkNotNull(actionsCellRenderer);
    this.configEditorPopupBuilder = Preconditions.checkNotNull(configEditorPopupBuilder);
    this.configUploaderPopupBuilder = Preconditions.checkNotNull(configUploaderPopupBuilder);

    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.eventHandlers = Preconditions.checkNotNull(eventHandlers);

    this.icons = Preconditions.checkNotNull(icons);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setLayout(new FitLayout());

    createGrid();
    createButtons();
  }

  private void createGrid() {
    ColumnModel cm = new ColumnModel(createColumnConfigs());

    RpcProxy<List<ConstraintConfigModel>> proxy = new RpcProxy<List<ConstraintConfigModel>>() {
      @Override
      protected void load(Object loadConfig, AsyncCallback<List<ConstraintConfigModel>> callback) {
        eventBus.fireEvent(new ConstraintConfigLoad.Event(callback));
      }
    };
    ListLoader<ListLoadResult<ConstraintConfigModel>> loader =
        new BaseListLoader<ListLoadResult<ConstraintConfigModel>>(proxy);
    ListStore<ConstraintConfigModel> store = new ListStore<ConstraintConfigModel>(loader);

    final Grid<ConstraintConfigModel> grid = new Grid<ConstraintConfigModel>(store, cm);
    grid.setAutoExpandColumn(ConstraintConfigModel.Attributes.NAME.id());
    grid.setBorders(true);
    grid.setColumnLines(true);
    grid.setColumnResize(false);
    grid.setHeight(600);

    grid.setSelectionModel(new GridSelectionModel<ConstraintConfigModel>() {
      @Override
      protected void onSelectChange(ConstraintConfigModel model, boolean select) {
        super.onSelectChange(model, select);

        eventBus.fireEvent(new ConstraintConfigFocus.Event(model));
      }
    });
    grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    eventHandlers.attach(grid);

    loader.load();
    add(grid);
  }

  private List<ColumnConfig> createColumnConfigs() {
    List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

    ColumnConfig nameColumn = new ColumnConfig();
    setCommonColumnProperties(nameColumn,
        ConstraintConfigModel.Attributes.NAME.id(),
        ConstraintConfigModel.Attributes.NAME.title(),
        325);
    configs.add(nameColumn);

    ColumnConfig authorColumn = new ColumnConfig();
    setCommonColumnProperties(authorColumn,
        ConstraintConfigModel.Attributes.AUTHOR.id(),
        ConstraintConfigModel.Attributes.AUTHOR.title(),
        300);
    configs.add(authorColumn);

    ColumnConfig modifiedColumn = new ColumnConfig();
    setCommonColumnProperties(modifiedColumn,
        ConstraintConfigModel.Attributes.MODIFIED.id(),
        ConstraintConfigModel.Attributes.MODIFIED.title(),
        150);
    modifiedColumn.setDateTimeFormat(DateTimeFormat.getFormat("HH:mm dd/MM/yyyy"));
    configs.add(modifiedColumn);

    ColumnConfig actionsColumn = new ColumnConfig();
    setCommonColumnProperties(actionsColumn,
        "actions",
        "Actions",
        120);
    actionsColumn.setRenderer(actionsCellRenderer);
    configs.add(actionsColumn);

    return configs;
  }

  private void setCommonColumnProperties(ColumnConfig column, String id, String header, int width) {
    column.setId(id);
    column.setHeader(header);
    column.setWidth(width);
    column.setMenuDisabled(true);
    column.setSortable(false);
  }

  private void createButtons() {
    ButtonBar buttonBar = new ButtonBar();

    Button addButton = new Button();
    addButton.setText("Add Constraint Config");
    addButton.setIcon(icons.addConfig());
    addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent ce) {
        configEditorPopupBuilder.build().show();
      }
    });
    buttonBar.add(addButton);

    Button uploadButton = new Button();
    uploadButton.setText("Upload Constraint Config");
    uploadButton.setIcon(icons.uploadConfig());
    uploadButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent ce) {
        configUploaderPopupBuilder.build().show();
      }
    });
    buttonBar.add(uploadButton);

    add(buttonBar, new FitData(5, 0, 0, 0));
  }

}

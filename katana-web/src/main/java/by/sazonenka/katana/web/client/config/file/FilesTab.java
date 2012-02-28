package by.sazonenka.katana.web.client.config.file;

import by.sazonenka.katana.web.client.config.dnd.FilesRulesDndSupport;
import by.sazonenka.katana.web.client.config.rule.RulesList;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class FilesTab extends TabItem {

  private final FilesTree filesTree;
  private final RulesList rulesList;
  private final FilesToolBar toolBar;

  private final FilesEventHandlers eventHandlers;
  private final FilesRulesDndSupport dndSupport;

  private ContentPanel container;

  @Inject
  public FilesTab(FilesTree filesTree, RulesList rulesList, FilesToolBar toolBar,
      FilesEventHandlers eventHandlers, FilesRulesDndSupport dndSupport) {
    this.filesTree = Preconditions.checkNotNull(filesTree);
    this.rulesList = Preconditions.checkNotNull(rulesList);
    this.toolBar = Preconditions.checkNotNull(toolBar);
    this.eventHandlers = Preconditions.checkNotNull(eventHandlers);
    this.dndSupport = Preconditions.checkNotNull(dndSupport);

    createContainer();
  }

  private void createContainer() {
    setLayout(new FitLayout());
    setText("Output Files");

    container = new ContentPanel();
    container.setLayout(new BorderLayout());

    container.setHeaderVisible(false);
    container.setBodyBorder(false);
    container.setBottomComponent(toolBar);

    add(container);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    addCenterBox();
    addEastBox();

    dndSupport.attach(filesTree, rulesList);
  }

  private void addCenterBox() {
    ContentPanel centerBox = new ContentPanel();

    centerBox.setLayout(new FitLayout());

    centerBox.setHeaderVisible(false);
    centerBox.setBorders(false);

    centerBox.add(filesTree);
    eventHandlers.attach(filesTree);

    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.setMargins(new Margins(5));
    container.add(centerBox, centerData);
  }

  private void addEastBox() {
    ContentPanel eastBox = new ContentPanel();

    eastBox.setLayout(new FitLayout());

    eastBox.setHeaderVisible(false);
    eastBox.setBorders(false);

    eastBox.add(rulesList);

    BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 300, 200, 500);
    eastData.setMargins(new Margins(5, 5, 5, 0));
    eastData.setSplit(true);
    container.add(eastBox, eastData);
  }

}

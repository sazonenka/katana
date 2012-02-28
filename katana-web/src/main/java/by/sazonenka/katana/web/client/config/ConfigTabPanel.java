package by.sazonenka.katana.web.client.config;

import by.sazonenka.katana.web.client.config.file.FilesTab;
import by.sazonenka.katana.web.client.config.rule.RulesTab;
import by.sazonenka.katana.web.client.config.xml.XmlTab;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConfigTabPanel extends LayoutContainer {

  private final RulesTab rulesTab;
  private final FilesTab filesTab;
  private final XmlTab xmlTab;

  @Inject
  public ConfigTabPanel(RulesTab rulesTab, FilesTab filesTab, XmlTab xmlTab) {
    this.rulesTab = Preconditions.checkNotNull(rulesTab);
    this.filesTab = Preconditions.checkNotNull(filesTab);
    this.xmlTab = Preconditions.checkNotNull(xmlTab);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setLayout(new FitLayout());

    createTabPanel();
  }

  private void createTabPanel() {
    TabPanel tabPanel = new TabPanel();
    tabPanel.setPlain(true);
    tabPanel.setHeight(650);

    tabPanel.add(rulesTab);
    tabPanel.add(filesTab);
    tabPanel.add(xmlTab);

    add(tabPanel);
  }

}

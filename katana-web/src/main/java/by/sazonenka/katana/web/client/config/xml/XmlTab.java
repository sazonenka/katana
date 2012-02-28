package by.sazonenka.katana.web.client.config.xml;

import by.sazonenka.katana.web.client.events.ConstraintConfigXmlLoad;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class XmlTab extends TabItem {

  private final EventBus eventBus;
  private final XmlEventHandlers eventHandlers;

  private ContentPanel container;

  @Inject
  public XmlTab(EventBus eventBus, XmlEventHandlers eventHandlers) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.eventHandlers = Preconditions.checkNotNull(eventHandlers);

    createContainer();
  }

  private void createContainer() {
    setLayout(new FitLayout());
    setText("XML");

    container = new ContentPanel();
    container.setLayout(new FitLayout());
    container.setScrollMode(Scroll.AUTOY);

    container.setHeaderVisible(false);
    container.setBodyBorder(false);

    add(container);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    eventHandlers.attach(container);
    eventBus.fireEvent(new ConstraintConfigXmlLoad.Event());
  }

  @Override
  protected void onShow() {
    super.onShow();

    eventBus.fireEvent(new ConstraintConfigXmlLoad.Event());
  }

}

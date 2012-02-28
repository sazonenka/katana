package by.sazonenka.katana.web.client.config.rule;

import by.sazonenka.katana.web.client.events.ValidationRuleLoad;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class RulesTab extends TabItem {

  private final RulesToolBar toolBar;

  private final EventBus eventBus;
  private final RulesEventHandlers eventHandlers;

  @Inject
  public RulesTab(RulesToolBar toolBar, EventBus eventBus, RulesEventHandlers eventHandlers) {
    this.toolBar = Preconditions.checkNotNull(toolBar);
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.eventHandlers = Preconditions.checkNotNull(eventHandlers);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setLayout(new FitLayout());
    setText("Validation Rules");

    ContentPanel container = new ContentPanel();
    container.setLayout(new FlowLayout());
    container.setScrollMode(Scroll.AUTOY);

    container.setHeaderVisible(false);
    container.setBorders(false);
    container.setBodyBorder(false);
    container.setBottomComponent(toolBar);

    add(container);

    eventHandlers.attach(container);
    eventBus.fireEvent(new ValidationRuleLoad.Event());
  }

}

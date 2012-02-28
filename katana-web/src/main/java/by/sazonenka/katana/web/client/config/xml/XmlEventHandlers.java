package by.sazonenka.katana.web.client.config.xml;

import by.sazonenka.katana.web.client.config.ConfigDictionary;
import by.sazonenka.katana.web.client.events.ConstraintConfigXmlLoad;
import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManagerAsync;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class XmlEventHandlers {

  private final EventBus eventBus;
  private final ConfigDictionary dictionary;
  private final ConstraintConfigManagerAsync configManager;

  @Inject
  public XmlEventHandlers(EventBus eventBus, ConfigDictionary dictionary,
      ConstraintConfigManagerAsync configManager) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
    this.dictionary = Preconditions.checkNotNull(dictionary);
    this.configManager = Preconditions.checkNotNull(configManager);
  }

  public void attach(final ContentPanel xmlTab) {
    eventBus.addHandler(ConstraintConfigXmlLoad.Event.getType(),
        new ConstraintConfigXmlLoad.Handler() {
          @Override
          public void onLoad(ConstraintConfigXmlLoad.Event event) {
            configManager.loadXml(dictionary.getConfigId(), new GenericAsyncCallback<String>() {
              @Override
              public void onSuccess(String xml) {
                String safeXml = SafeHtmlUtils.htmlEscape(xml);
                String html = "<pre style='padding: 5px'>" + safeXml + "</pre>";

                xmlTab.removeAll();
                xmlTab.addText(html);
                xmlTab.layout();
              }
            });
          }
        });
  }

}

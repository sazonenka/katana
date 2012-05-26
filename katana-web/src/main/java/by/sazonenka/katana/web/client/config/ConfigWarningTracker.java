package by.sazonenka.katana.web.client.config;

import java.util.List;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.core.El;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import by.sazonenka.katana.web.client.events.util.GenericAsyncCallback;
import by.sazonenka.katana.web.client.managers.ConstraintConfigManagerAsync;
import by.sazonenka.katana.web.client.resources.Icons;

@Singleton
public final class ConfigWarningTracker {

  private final Icons icons;
  private final ConfigDictionary dictionary;
  private final ConstraintConfigManagerAsync configManager;

  private final List<Element> cells;
  private final List<Warning> warnings;

  @Inject
  public ConfigWarningTracker(Icons icons, ConfigDictionary dictionary,
      ConstraintConfigManagerAsync configManager) {
    this.icons = Preconditions.checkNotNull(icons);
    this.dictionary = Preconditions.checkNotNull(dictionary);
    this.configManager = Preconditions.checkNotNull(configManager);

    this.cells = Lists.newArrayList();
    this.warnings = Lists.newArrayList();
  }

  public void register(El row) {
    Element td = DOM.createTD();
    td.setInnerHTML(icons.warningConfig().getHTML() + "&nbsp;&nbsp;");

    setVisibilityAndTitleBasedOnWarnings(td);

    cells.add(td);
    row.insertFirst(td);
  }

  public void updateState() {
    configManager.checkConfigForNameDuplicates(dictionary.getConfigId(),
        new GenericAsyncCallback<List<Warning>>() {
          @Override
          public void onSuccess(List<Warning> newWarnings) {
            warnings.clear();
            warnings.addAll(newWarnings);

            for (Element cell : cells) {
              setVisibilityAndTitleBasedOnWarnings(cell);
            }
          }
        });
  }

  private void setVisibilityAndTitleBasedOnWarnings(Element td) {
    if (warnings.isEmpty()) {
      td.setClassName(HideMode.DISPLAY.value());
      td.setTitle("");
    } else {
      td.removeClassName(HideMode.DISPLAY.value());
      td.setTitle("You got name duplicates in your " + Joiner.on(", ").join(warnings));
    }
  }

  public enum Warning {
    RULE_NAME_DUPLICATE("rules"),
    FILE_NAME_DUPLICATE("files"),
    FIELD_NAME_DUPLICATE("fields");

    private final String description;

    private Warning(String description) {
      this.description = description;
    }

    @Override
    public String toString() { return description; }
  }

}

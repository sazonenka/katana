package by.sazonenka.katana.web.client.config.dnd;

import by.sazonenka.katana.web.client.config.file.FilesTree;
import by.sazonenka.katana.web.client.config.rule.RulesList;

import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class FilesRulesDndSupport {

  private final EventBus eventBus;

  @Inject
  public FilesRulesDndSupport(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  public void attach(FilesTree filesTree, RulesList rulesList) {
    new TreePanelDragSource(filesTree);

    new FilesTreeDropTarget(filesTree, eventBus);
    new RulesListDropTarget(rulesList, eventBus);
  }

}

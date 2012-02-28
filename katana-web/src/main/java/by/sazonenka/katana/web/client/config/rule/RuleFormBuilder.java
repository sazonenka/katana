package by.sazonenka.katana.web.client.config.rule;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class RuleFormBuilder {

  private final EventBus eventBus;

  @Inject
  public RuleFormBuilder(EventBus eventBus) {
    this.eventBus = Preconditions.checkNotNull(eventBus);
  }

  public RuleForm build(ValidationRuleModel model) {
    return new RuleForm(eventBus, model);
  }

}

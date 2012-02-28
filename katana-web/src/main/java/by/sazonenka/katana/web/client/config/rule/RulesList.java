package by.sazonenka.katana.web.client.config.rule;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aliaksandr Sazonenka
 */
@Singleton
public final class RulesList extends ListView<ValidationRuleModel> {

  @Inject
  public RulesList(ListStore<ValidationRuleModel> store) {
    super(Preconditions.checkNotNull(store));
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setBorders(false);

    setSimpleTemplate("{name}");
    getSelectionModel().setLocked(true);
  }

}

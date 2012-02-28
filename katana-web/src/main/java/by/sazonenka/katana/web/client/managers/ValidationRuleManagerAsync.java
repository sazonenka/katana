package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public interface ValidationRuleManagerAsync {

  void findByConfig(Long configId, AsyncCallback<List<ValidationRuleModel>> callback);

  void save(ValidationRuleModel model, AsyncCallback<ValidationRuleModel> callback);

  void swap(Long ruleId1, Long ruleId2, AsyncCallback<Void> callback);

  void delete(Long id, AsyncCallback<Void> callback);

}

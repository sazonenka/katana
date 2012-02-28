package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.ValidationRuleModel;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Aliaksandr Sazonenka
 */
public interface ValidationRuleManager extends RemoteService {

  List<ValidationRuleModel> findByConfig(Long configId) throws Exception;

  ValidationRuleModel save(ValidationRuleModel model) throws Exception;

  void swap(Long ruleId1, Long ruleId2) throws Exception;

  void delete(Long id) throws Exception;

}

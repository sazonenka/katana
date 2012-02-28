package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.client.config.ConfigWarningTracker.Warning;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public interface ConstraintConfigManagerAsync {

  void findAll(AsyncCallback<List<ConstraintConfigModel>> callback);

  void save(ConstraintConfigModel model, AsyncCallback<ConstraintConfigModel> callback);

  void delete(Long id, AsyncCallback<Void> callback);

  void loadXml(Long id, AsyncCallback<String> callback);

  void checkConfigForNameDuplicates(Long id, AsyncCallback<List<Warning>> callback);

}

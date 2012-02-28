package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFieldModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public interface OutputFieldManagerAsync {

  void findByFile(Long configId, AsyncCallback<List<OutputFieldModel>> callback);

  void save(OutputFieldModel model, AsyncCallback<OutputFieldModel> callback);

  void delete(List<Long> ids, AsyncCallback<Void> callback);

  void reorder(List<Long> ids, AsyncCallback<Void> callback);

  void map(List<Long> ids, Long ruleId, AsyncCallback<Void> callback);

  void unmap(List<Long> ids, AsyncCallback<Void> callback);

  void refresh(List<Long> ids, AsyncCallback<List<OutputFieldModel>> callback);

}

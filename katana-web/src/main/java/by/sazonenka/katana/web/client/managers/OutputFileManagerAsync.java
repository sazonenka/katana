package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Aliaksandr Sazonenka
 */
public interface OutputFileManagerAsync {

  void findByConfig(Long configId, AsyncCallback<List<OutputFileModel>> callback);

  void save(OutputFileModel model, AsyncCallback<OutputFileModel> callback);

  void delete(List<Long> ids, AsyncCallback<Void> callback);

  void reorder(List<Long> ids, AsyncCallback<Void> callback);

  void map(List<Long> ids, Long parentId, AsyncCallback<Boolean> callback);

  void unmap(List<Long> ids, AsyncCallback<Void> callback);

  void refresh(List<Long> ids, AsyncCallback<List<OutputFileModel>> callback);

}

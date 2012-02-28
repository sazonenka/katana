package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFileModel;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Aliaksandr Sazonenka
 */
public interface OutputFileManager extends RemoteService {

  List<OutputFileModel> findByConfig(Long configId) throws Exception;

  OutputFileModel save(OutputFileModel model) throws Exception;

  void delete(List<Long> ids) throws Exception;

  void reorder(List<Long> ids) throws Exception;

  boolean map(List<Long> ids, Long parentId) throws Exception;

  void unmap(List<Long> ids) throws Exception;

  List<OutputFileModel> refresh(List<Long> ids) throws Exception;

}

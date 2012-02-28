package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.model.OutputFieldModel;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Aliaksandr Sazonenka
 */
public interface OutputFieldManager extends RemoteService {

  List<OutputFieldModel> findByFile(Long fileId) throws Exception;

  OutputFieldModel save(OutputFieldModel model) throws Exception;

  void delete(List<Long> ids) throws Exception;

  void reorder(List<Long> ids) throws Exception;

  void map(List<Long> ids, Long ruleId) throws Exception;

  void unmap(List<Long> ids) throws Exception;

  List<OutputFieldModel> refresh(List<Long> ids) throws Exception;

}

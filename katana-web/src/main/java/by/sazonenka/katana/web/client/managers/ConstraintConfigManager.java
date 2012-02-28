package by.sazonenka.katana.web.client.managers;

import java.util.List;

import by.sazonenka.katana.web.client.config.ConfigWarningTracker.Warning;
import by.sazonenka.katana.web.model.ConstraintConfigModel;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Aliaksandr Sazonenka
 */
public interface ConstraintConfigManager extends RemoteService {

  List<ConstraintConfigModel> findAll() throws Exception;

  ConstraintConfigModel save(ConstraintConfigModel model) throws Exception;

  void delete(Long id) throws Exception;

  String loadXml(Long id) throws Exception;

  List<Warning> checkConfigForNameDuplicates(Long id) throws Exception;

}

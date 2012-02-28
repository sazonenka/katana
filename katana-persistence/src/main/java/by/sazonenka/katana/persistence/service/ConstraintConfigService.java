package by.sazonenka.katana.persistence.service;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.xml.service.XmlPersisterException;
import by.sazonenka.katana.xml.service.XmlValidatorException;

/**
 * Interface that specifies a set of business operations
 * which can be performed on {@link ConstraintConfig} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericService
 */
public interface ConstraintConfigService extends GenericService<ConstraintConfig, Long> {

  /** Returns a list of all {@link ConstraintConfig}s. */
  List<ConstraintConfig> findAll();

  long getCount();

  byte[] saveToBuffer(Long configId) throws XmlPersisterException;

  String saveToString(Long configId) throws XmlPersisterException;

  void loadFromBuffer(byte[] configSource) throws XmlPersisterException, XmlValidatorException;

}

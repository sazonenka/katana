package by.sazonenka.katana.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import by.sazonenka.katana.persistence.converter.XmlConverter;
import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.service.ConstraintConfigService;
import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.service.XmlPersister;
import by.sazonenka.katana.xml.service.XmlPersisterException;
import by.sazonenka.katana.xml.service.XmlValidator;
import by.sazonenka.katana.xml.service.XmlValidatorException;

import com.google.common.base.Preconditions;

/**
 * This class implements business operations with {@link ConstraintConfig}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Service
public final class ConstraintConfigServiceImpl
    extends GenericServiceImpl<ConstraintConfig, Long, ConstraintConfigDao>
    implements ConstraintConfigService {

  private final XmlConverter converter;
  private final XmlPersister persister;
  private final XmlValidator validator;

  @Inject
  public ConstraintConfigServiceImpl(ConstraintConfigDao configDao, XmlConverter converter,
      XmlPersister persister, XmlValidator validator) {
    super(configDao);

    this.converter = Preconditions.checkNotNull(converter);
    this.persister = Preconditions.checkNotNull(persister);
    this.validator = Preconditions.checkNotNull(validator);
  }

  @Override
  public List<ConstraintConfig> findAll() {
    return dao.findAll();
  }

  @Override
  public long getCount() {
    return dao.getCount();
  }

  @Override
  public byte[] saveToBuffer(Long configId) throws XmlPersisterException {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    return persister.saveToBuffer(converter.loadConfigXml(configId));
  }

  @Override
  public String saveToString(Long configId) throws XmlPersisterException {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    return persister.saveToString(converter.loadConfigXml(configId));
  }

  @Override
  public void loadFromBuffer(byte[] configSource)
      throws XmlPersisterException, XmlValidatorException {
    Preconditions.checkNotNull(configSource, "Got unexpected null buffer passed to the method.");

    ConstraintConfigXml configXml = persister.loadFromBuffer(configSource);

    validator.validate(configXml);
    converter.saveConfigXml(configXml);
  }

}

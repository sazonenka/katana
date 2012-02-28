package by.sazonenka.katana.persistence.converter;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * @author Aliaksandr Sazonenka
 */
public interface XmlConverter {

  ConstraintConfigXml loadConfigXml(Long configId);

  void saveConfigXml(ConstraintConfigXml configXml);

}

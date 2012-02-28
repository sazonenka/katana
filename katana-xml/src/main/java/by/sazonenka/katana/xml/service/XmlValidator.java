package by.sazonenka.katana.xml.service;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * @author Aliaksandr Sazonenka
 */
public interface XmlValidator {

  void validate(ConstraintConfigXml configXml) throws XmlValidatorException;

}

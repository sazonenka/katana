package by.sazonenka.katana.xml.service;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * Exposes a set of operations used to serialize and deserialize {@link ConstraintConfigXml}s.
 *
 * @author Aliaksandr Sazonenka
 */
public interface XmlPersister {

  /**
   * Serializes the given {@link ConstraintConfigXml} to a byte buffer.
   *
   * @param config the {@link ConstraintConfigXml} that is to be serialized
   * @return buffer where the serialized XML is written to
   * @throws XmlPersisterException if the schema for the {@link ConstraintConfigXml}
   *         is not valid, or there are some I/O issues
   */
  byte[] saveToBuffer(ConstraintConfigXml config) throws XmlPersisterException;

  /**
   * Serializes the given {@link ConstraintConfigXml} to a string.
   *
   * @param config the {@link ConstraintConfigXml} that is to be serialized
   * @return string where the serialized XML is written to
   * @throws XmlPersisterException if the schema for the {@link ConstraintConfigXml}
   *         is not valid, or there are some I/O issues
   */
  String saveToString(ConstraintConfigXml config) throws XmlPersisterException;

  /**
   * Deserializes a {@link ConstraintConfigXml} from the buffer.
   *
   * @param buffer provides the source of the XML document
   * @return the {@link ConstraintConfigXml} deserialized from the XML document
   * @throws XmlPersisterException if the XML document doesn't fully match
   *         the class XML schema, or there are some I/O issues
   */
  ConstraintConfigXml loadFromBuffer(byte[] buffer) throws XmlPersisterException;

}

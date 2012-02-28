package by.sazonenka.katana.xml.service;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;

/**
 * Exception to indicate an error in serializing/deserializing of a {@link ConstraintConfigXml}.
 *
 * @author Aliaksandr Sazonenka
 */
public final class XmlPersisterException extends Exception {

  private static final long serialVersionUID = 5945589383168438903L;

  /**
   * Constructs a new instance of the {@link XmlPersisterException}.
   *
   * @param message the error message
   */
  public XmlPersisterException(String message) {
    super(message);
  }

  /**
   * Constructs a new instance of the {@link XmlPersisterException}.
   *
   * @param message the error message
   * @param cause the exception or error that caused this exception to be thrown
   */
  public XmlPersisterException(String message, Throwable cause) {
    super(message, cause);
  }

}

package by.sazonenka.katana.xml.service;

/**
 * @author Aliaksandr Sazonenka
 */
public final class XmlValidatorException extends Exception {

  private static final long serialVersionUID = 5032452457271078430L;

  /**
   * Constructs a new instance of the {@link XmlValidatorException}.
   *
   * @param message the error message
   */
  public XmlValidatorException(String message) {
    super(message);
  }

}

package by.sazonenka.katana.persistence.service;

import java.util.Date;
import java.util.GregorianCalendar;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ServiceTestData {

  /* ConstraintConfig constants. */

  public static final Long CONFIGS_COUNT = 1L;

  public static final Long CONFIG_1_ID = 1L;
  private static final String CONFIG_1_NAME = "config-1";
  private static final String CONFIG_1_AUTHOR = "";
  private static final Date CONFIG_1_MODIFIED = new GregorianCalendar(2010, 0, 1).getTime();

  public static ConstraintConfig getConfig1() {
    return new ConstraintConfig(CONFIG_1_NAME, CONFIG_1_AUTHOR, CONFIG_1_MODIFIED);
  }

  /* ValidationRule constants. */

  public static final Long RULES_IN_CONFIG_1 = 1L;

  public static final Long RULE_1_ID = 1L;
  private static final String RULE_1_NAME = "rule-1";
  private static final boolean RULE_1_NULLABLE = true;
  private static final String RULE_1_REGEXP = "regexp-1";
  private static final String RULE_1_DESCRIPTION = null;
  private static final int RULE_1_ORDER_IN_CONFIG = 0;

  public static ValidationRule getRule1() {
    return new ValidationRule(RULE_1_NAME, RULE_1_NULLABLE, RULE_1_REGEXP, RULE_1_DESCRIPTION,
        RULE_1_ORDER_IN_CONFIG, getConfig1());
  }

  /* OutputFile constants. */

  public static final Long FILES_IN_CONFIG_1 = 2L;

  public static final Long FILE_1_ID = 1L;
  private static final String FILE_1_NAME = "file-1.csv";
  private static final int FILE_1_ORDER_IN_CONFIG = 0;

  public static final Long FILE_2_ID = 2L;
  private static final String FILE_2_NAME = "file-2.csv";
  private static final int FILE_2_ORDER_IN_CONFIG = 1;

  public static OutputFile getFile1() {
    return new OutputFile(FILE_1_NAME, FILE_1_ORDER_IN_CONFIG, null, getConfig1());
  }

  public static OutputFile getFile2() {
    return new OutputFile(FILE_2_NAME, FILE_2_ORDER_IN_CONFIG, getFile1(), getConfig1());
  }

  /* OutputField constants. */

  public static final Long FIELDS_IN_FILE_1 = 1L;

  public static final Long FIELD_1_ID = 1L;
  private static final String FIELD_1_NAME = "field-1";
  private static final int FIELD_1_ORDER_IN_FILE = 0;

  public static final Long FIELDS_IN_FILE_2 = 1L;

  public static final Long FIELD_2_ID = 2L;
  private static final String FIELD_2_NAME = "field-2";
  private static final int FIELD_2_ORDER_IN_FILE = 0;

  public static OutputField getField1() {
    return new OutputField(FIELD_1_NAME, FIELD_1_ORDER_IN_FILE, getFile1(), getRule1());
  }

  public static OutputField getField2() {
    return new OutputField(FIELD_2_NAME, FIELD_2_ORDER_IN_FILE, getFile2(), getRule1());
  }

  private ServiceTestData() {
    /* Ensure non-instanciability. */
  }

}

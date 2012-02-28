package by.sazonenka.katana.persistence.dao;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Aliaksandr Sazonenka
 */
public final class DaoTestData {

  /* ConstraintConfig constants. */

  public static final Long CONFIG_EXIST_ID = 1L;
  public static final String CONFIG_EXIST_NAME = "config-1";
  public static final String CONFIG_EXIST_AUTHOR = "author-1";
  public static final Date CONFIG_EXIST_MODIFIED = new GregorianCalendar(2010, 0, 1).getTime();

  public static final Long CONFIG_NONEXIST_ID = 3L;
  public static final String CONFIG_NONEXIST_NAME = "config-3";
  public static final String CONFIG_NONEXIST_AUTHOR = "author-3";
  public static final Date CONFIG_NONEXIST_MODIFIED = new GregorianCalendar(2012, 0, 1).getTime();

  /* ValidationRule constants. */

  public static final Long RULE_EXIST_ID = 1L;
  public static final String RULE_EXIST_NAME = "rule-1";
  public static final boolean RULE_EXIST_NULLABLE = true;
  public static final String RULE_EXIST_REGEXP = "regexp-1";
  public static final String RULE_EXIST_DESCRIPTION = null;
  public static final int RULE_EXIST_ORDER = 0;

  public static final Long RULE_NONEXIST_ID = 5L;
  public static final String RULE_NONEXIST_NAME = "rule-5";
  public static final boolean RULE_NONEXIST_NULLABLE = false;
  public static final String RULE_NONEXIST_REGEXP = "regexp-5";
  public static final String RULE_NONEXIST_DESCRIPTION = "description-5";
  public static final int RULE_NONEXIST_ORDER = 1;

  /* OutputFile constants. */

  public static final Long FILE_EXIST_ID = 1L;
  public static final String FILE_EXIST_NAME = "file-1.csv";
  public static final int FILE_EXIST_ORDER = 0;

  public static final Long FILE_NONEXIST_ID = 9L;
  public static final String FILE_NONEXIST_NAME = "file-9.csv";
  public static final int FILE_NONEXIST_ORDER = 1;

  /* OutputField constants. */

  public static final Long FIELD_EXIST_ID = 1L;
  public static final String FIELD_EXIST_NAME = "field-1";
  public static final int FIELD_EXIST_ORDER = 0;

  public static final Long FIELD_NONEXIST_ID = 13L;
  public static final String FIELD_NONEXIST_NAME = "field-13";
  public static final int FIELD_NONEXIST_ORDER = 1;

  private DaoTestData() {
    /* Ensure non-instanciability. */
  }

}

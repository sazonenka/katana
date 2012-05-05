package by.sazonenka.katana.web.server;

import java.util.Date;
import java.util.GregorianCalendar;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.web.model.ConstraintConfigModel;
import by.sazonenka.katana.web.model.OutputFieldModel;
import by.sazonenka.katana.web.model.OutputFileModel;
import by.sazonenka.katana.web.model.ValidationRuleModel;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ManagerTestData {

  /* ConstraintConfig constants. */

  public static final Long CONFIG_1_ID = 1L;
  private static final String CONFIG_1_NAME = "config-1";
  private static final String CONFIG_1_AUTHOR = "";
  private static final Date CONFIG_1_MODIFIED = new GregorianCalendar(2010, 0, 1).getTime();

  public static ConstraintConfig getConfig1() {
    ConstraintConfig config = getNewConfig1();
    config.setId(CONFIG_1_ID);
    return config;
  }

  public static ConstraintConfig getNewConfig1() {
    return new ConstraintConfig(CONFIG_1_NAME, CONFIG_1_AUTHOR, CONFIG_1_MODIFIED);
  }

  public static ConstraintConfigModel getConfigModel1() {
    ConstraintConfigModel config = getNewConfigModel1();
    config.setId(CONFIG_1_ID);
    return config;
  }

  public static ConstraintConfigModel getNewConfigModel1() {
    ConstraintConfigModel config = new ConstraintConfigModel();

    config.setName(CONFIG_1_NAME);
    config.setAuthor(CONFIG_1_AUTHOR);
    config.setModified(CONFIG_1_MODIFIED);

    return config;
  }

  /* ValidationRule constants. */

  public static final Long RULE_1_ID = 1L;
  private static final String RULE_1_NAME = "rule-1";
  private static final boolean RULE_1_NULLABLE = true;
  private static final String RULE_1_REGEXP = "regexp-1";
  private static final String RULE_1_DESCRIPTION = null;
  private static final int RULE_1_ORDER_IN_CONFIG = 0;

  public static final Long RULE_2_ID = 2L;
  private static final String RULE_2_NAME = "rule-2";
  private static final boolean RULE_2_NULLABLE = false;
  private static final String RULE_2_REGEXP = "regexp-2";
  private static final String RULE_2_DESCRIPTION = null;
  private static final int RULE_2_ORDER_IN_CONFIG = 1;

  public static ValidationRule getRule1() {
    ValidationRule rule = getNewRule1();
    rule.setId(RULE_1_ID);
    return rule;
  }

  public static ValidationRule getNewRule1() {
    return new ValidationRule(RULE_1_NAME, RULE_1_NULLABLE, RULE_1_REGEXP, RULE_1_DESCRIPTION,
        RULE_1_ORDER_IN_CONFIG, getConfig1());
  }

  public static ValidationRule getRule2() {
    ValidationRule rule = getNewRule2();
    rule.setId(RULE_2_ID);
    return rule;
  }

  public static ValidationRule getNewRule2() {
    return new ValidationRule(RULE_2_NAME, RULE_2_NULLABLE, RULE_2_REGEXP, RULE_2_DESCRIPTION,
        RULE_2_ORDER_IN_CONFIG, getConfig1());
  }

  public static ValidationRuleModel getRuleModel1() {
    ValidationRuleModel rule = getNewRuleModel1();
    rule.setId(RULE_1_ID);
    return rule;
  }

  public static ValidationRuleModel getNewRuleModel1() {
    ValidationRuleModel rule = new ValidationRuleModel();

    rule.setName(RULE_1_NAME);
    rule.setNullable(RULE_1_NULLABLE);
    rule.setRegexp(RULE_1_REGEXP);
    rule.setDescription(RULE_1_DESCRIPTION);
    rule.setConfig(CONFIG_1_ID);

    return rule;
  }

  public static ValidationRuleModel getRuleModel2() {
    ValidationRuleModel rule = getNewRuleModel2();
    rule.setId(RULE_2_ID);
    return rule;
  }

  public static ValidationRuleModel getNewRuleModel2() {
    ValidationRuleModel rule = new ValidationRuleModel();

    rule.setName(RULE_2_NAME);
    rule.setNullable(RULE_2_NULLABLE);
    rule.setRegexp(RULE_2_REGEXP);
    rule.setDescription(RULE_2_DESCRIPTION);
    rule.setConfig(CONFIG_1_ID);

    return rule;
  }

  /* OutputFile constants. */

  public static final Long FILE_1_ID = 1L;
  private static final String FILE_1_NAME = "file-1.csv";
  private static final int FILE_1_ORDER_IN_CONFIG = 0;

  public static final Long FILE_2_ID = 2L;
  private static final String FILE_2_NAME = "file-2.csv";
  private static final int FILE_2_ORDER_IN_CONFIG = 1;

  public static final Long FILE_3_ID = 3L;
  private static final String FILE_3_NAME = "file-3.csv";
  private static final int FILE_3_ORDER_IN_CONFIG = 2;

  public static OutputFile getFile1() {
    OutputFile file = getNewFile1();
    file.setId(FILE_1_ID);
    return file;
  }

  public static OutputFile getNewFile1() {
    return new OutputFile(FILE_1_NAME, FILE_1_ORDER_IN_CONFIG, null, getConfig1());
  }

  public static OutputFile getFile2() {
    OutputFile file = getNewFile2();
    file.setId(FILE_2_ID);
    return file;
  }

  public static OutputFile getNewFile2() {
    return new OutputFile(FILE_2_NAME, FILE_2_ORDER_IN_CONFIG, getFile1(), getConfig1());
  }

  public static OutputFile getFile3() {
    OutputFile file = getNewFile3();
    file.setId(FILE_3_ID);
    return file;
  }

  public static OutputFile getNewFile3() {
    return new OutputFile(FILE_3_NAME, FILE_3_ORDER_IN_CONFIG, getFile1(), getConfig1());
  }

  public static OutputFileModel getFileModel1() {
    OutputFileModel file = getNewFileModel1();
    file.setId(FILE_1_ID);
    return file;
  }

  public static OutputFileModel getNewFileModel1() {
    OutputFileModel file = new OutputFileModel();

    file.setName(FILE_1_NAME);
    file.setConfig(CONFIG_1_ID);
    file.setParentFile(null);

    return file;
  }

  public static OutputFileModel getFileModel2() {
    OutputFileModel file = getNewFileModel2();
    file.setId(FILE_2_ID);
    return file;
  }

  public static OutputFileModel getNewFileModel2() {
    OutputFileModel file = new OutputFileModel();

    file.setName(FILE_2_NAME);
    file.setConfig(CONFIG_1_ID);
    file.setParentFile(FILE_1_NAME);

    return file;
  }

  public static OutputFileModel getFileModel3() {
    OutputFileModel file = getNewFileModel3();
    file.setId(FILE_3_ID);
    return file;
  }

  public static OutputFileModel getNewFileModel3() {
    OutputFileModel file = new OutputFileModel();

    file.setName(FILE_3_NAME);
    file.setConfig(CONFIG_1_ID);
    file.setParentFile(FILE_1_NAME);

    return file;
  }

  /* OutputField constants. */

  public static final Long FIELD_1_ID = 1L;
  private static final String FIELD_1_NAME = "field-1";
  private static final int FIELD_1_ORDER_IN_FILE = 0;

  public static final Long FIELD_2_ID = 2L;
  private static final String FIELD_2_NAME = "field-2";
  private static final int FIELD_2_ORDER_IN_FILE = 1;

  public static OutputField getField1() {
    OutputField field = getNewField1();
    field.setId(FIELD_1_ID);
    return field;
  }

  public static OutputField getNewField1() {
    return new OutputField(FIELD_1_NAME, FIELD_1_ORDER_IN_FILE, getFile1(), getRule1());
  }

  public static OutputField getField2() {
    OutputField field = getNewField2();
    field.setId(FIELD_2_ID);
    return field;
  }

  public static OutputField getNewField2() {
    return new OutputField(FIELD_2_NAME, FIELD_2_ORDER_IN_FILE, getFile2(), getRule1());
  }

  public static OutputFieldModel getFieldModel1() {
    OutputFieldModel field = getNewFieldModel1();
    field.setId(FIELD_1_ID);
    return field;
  }

  public static OutputFieldModel getNewFieldModel1() {
    OutputFieldModel field = new OutputFieldModel();

    field.setName(FIELD_1_NAME);
    field.setFile(FILE_1_ID);
    field.setRule(RULE_1_NAME);

    return field;
  }

  public static OutputFieldModel getFieldModel2() {
    OutputFieldModel field = getNewFieldModel2();
    field.setId(FIELD_2_ID);
    return field;
  }

  public static OutputFieldModel getNewFieldModel2() {
    OutputFieldModel field = new OutputFieldModel();

    field.setName(FIELD_2_NAME);
    field.setFile(FILE_2_ID);
    field.setRule(RULE_1_NAME);

    return field;
  }

  private ManagerTestData() {
    /* Ensure non-instanciability. */
  }

}

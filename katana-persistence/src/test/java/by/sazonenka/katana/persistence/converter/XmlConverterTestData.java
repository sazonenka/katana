package by.sazonenka.katana.persistence.converter;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.domain.ExtendsXml;
import by.sazonenka.katana.xml.domain.OutputFieldXml;
import by.sazonenka.katana.xml.domain.OutputFileXml;
import by.sazonenka.katana.xml.domain.ValidationRuleXml;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public final class XmlConverterTestData {

  /* Test constraint config constants. */

  private static final String DATASET_ID = "config-1";

  private static final String RULE_ID = "rule-1";
  private static final boolean RULE_NULLABLE = true;
  private static final String RULE_REGEXP = "regexp-1";
  private static final String RULE_DESCRIPTION = null;

  private static final String FILE_1_NAME = "file-1.csv";
  private static final String FILE_2_NAME = "file-2.csv";

  private static final String FIELD_1_NAME = "field-1";
  private static final String FIELD_2_NAME = "field-2";

  /* Correct constraint config builder. */

  public static ConstraintConfigXml createConfigXml() {
    ConstraintConfigXml constraintConfig = new ConstraintConfigXml();

    constraintConfig.setId(DATASET_ID);
    constraintConfig.setValidationRules(Lists.newArrayList(createRuleXml()));
    constraintConfig.setOutputFiles(Lists.newArrayList(
        createFileXml1(),
        createFileXml2()));

    return constraintConfig;
  }

  private static ValidationRuleXml createRuleXml() {
    ValidationRuleXml validationRule = new ValidationRuleXml();

    validationRule.setId(RULE_ID);
    validationRule.setNullable(RULE_NULLABLE);
    validationRule.setRegexp(RULE_REGEXP);
    validationRule.setDescription(RULE_DESCRIPTION);

    return validationRule;
  }

  private static OutputFileXml createFileXml1() {
    OutputFileXml outputFile = new OutputFileXml();

    outputFile.setName(FILE_1_NAME);
    outputFile.setFields(Lists.newArrayList(createFieldXml(FIELD_1_NAME)));

    return outputFile;
  }

  private static OutputFileXml createFileXml2() {
    OutputFileXml outputFile = new OutputFileXml();

    outputFile.setName(FILE_2_NAME);
    outputFile.setExtendsFile(createExtendsXml());
    outputFile.setFields(Lists.newArrayList(createFieldXml(FIELD_2_NAME)));

    return outputFile;
  }

  private static OutputFieldXml createFieldXml(String fieldName) {
    OutputFieldXml outputField = new OutputFieldXml();

    outputField.setName(fieldName);
    outputField.setRule(RULE_ID);

    return outputField;
  }

  private static ExtendsXml createExtendsXml() {
    ExtendsXml extendsXml = new ExtendsXml();

    extendsXml.setFile(FILE_1_NAME);

    return extendsXml;
  }

  private XmlConverterTestData() {
    /* Ensure non-instanciability. */
  }

}

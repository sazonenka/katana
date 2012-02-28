package by.sazonenka.katana.xml.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.domain.ExtendsXml;
import by.sazonenka.katana.xml.domain.OutputFieldXml;
import by.sazonenka.katana.xml.domain.OutputFileXml;
import by.sazonenka.katana.xml.domain.ValidationRuleXml;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ServiceTestData {

  /* Paths to test constraint config XMLs. */

  public static final List<String> VALID_CONFIGS = getFilesByDirectoryInClassPath("xml/valid/");
  public static final List<String> INVALID_BY_SCHEMA_CONFIGS = getFilesByDirectoryInClassPath("xml/invalid-by-schema/");
  public static final List<String> INVALID_BY_RULES_CONFIGS = getFilesByDirectoryInClassPath("xml/invalid-by-rules/");

  private static List<String> getFilesByDirectoryInClassPath(String directoryRelativePath) {
    List<String> files = Lists.newArrayList();
    try {
      File directory = new ClassPathResource(directoryRelativePath).getFile();
      for (String fileName : directory.list()) {
        files.add(directoryRelativePath + fileName);
      }
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    return files;
  }

  public static final String TEST_CONFIG = "xml/valid/025-extends-with-fields.xml";
  public static final String NOT_EXISTING_CONFIG = "xml/config-not-existing.xml";

  public static byte[] getBufferInClassPath(String relativePath) throws IOException {
    InputStream stream = ServiceTestData.class.getClassLoader().getResourceAsStream(relativePath);
    return ByteStreams.toByteArray(stream);
  }

  /* Test constraint config constants. */

  private static final String DATASET_ID = "config-1";

  private static final String RULE_ID = "rule-1";
  private static final boolean RULE_NULLABLE = false;
  private static final String RULE_REGEXP = "regexp-1";
  private static final String RULE_DESCRIPTION = null;

  private static final String FILE_NAME_1 = "file-1.csv";
  private static final String FILE_NAME_2 = "file-2.csv";

  private static final String FIELD_NAME_1 = "field-1";
  private static final String FIELD_NAME_2 = "field-2";

  /* Correct constraint config builder. */

  public static ConstraintConfigXml createConstraintConfig() {
    ConstraintConfigXml constraintConfig = new ConstraintConfigXml();

    constraintConfig.setId(DATASET_ID);
    constraintConfig.setValidationRules(Lists.newArrayList(createValidationRule()));
    constraintConfig.setOutputFiles(Lists.newArrayList(
        createOutputFile1(),
        createOutputFile2()));

    return constraintConfig;
  }

  private static ValidationRuleXml createValidationRule() {
    ValidationRuleXml validationRule = new ValidationRuleXml();

    validationRule.setId(RULE_ID);
    validationRule.setNullable(RULE_NULLABLE);
    validationRule.setRegexp(RULE_REGEXP);
    validationRule.setDescription(RULE_DESCRIPTION);

    return validationRule;
  }

  private static OutputFileXml createOutputFile1() {
    OutputFileXml outputFile = new OutputFileXml();

    outputFile.setName(FILE_NAME_1);
    outputFile.setFields(Lists.newArrayList(createOutputField(FIELD_NAME_1)));

    return outputFile;
  }

  private static OutputFileXml createOutputFile2() {
    OutputFileXml outputFile = new OutputFileXml();

    outputFile.setName(FILE_NAME_2);
    outputFile.setExtendsFile(createExtends());
    outputFile.setFields(Lists.newArrayList(createOutputField(FIELD_NAME_2)));

    return outputFile;
  }

  private static OutputFieldXml createOutputField(String fieldName) {
    OutputFieldXml outputField = new OutputFieldXml();

    outputField.setName(fieldName);
    outputField.setRule(RULE_ID);

    return outputField;
  }

  private static ExtendsXml createExtends() {
    ExtendsXml extendsXml = new ExtendsXml();

    extendsXml.setFile(FILE_NAME_1);

    return extendsXml;
  }

  private ServiceTestData() {
    /* Ensure non-instanciability. */
  }

}

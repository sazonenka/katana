package by.sazonenka.katana.xml.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.domain.ExtendsXml;
import by.sazonenka.katana.xml.domain.OutputFieldXml;
import by.sazonenka.katana.xml.domain.OutputFileXml;
import by.sazonenka.katana.xml.domain.ValidationRuleXml;

/**
 * @author Aliaksandr Sazonenka
 */
@Service
public final class XmlValidatorImpl implements XmlValidator {

  @Override
  public void validate(ConstraintConfigXml configXml) throws XmlValidatorException {
    checkRuleNamesForDuplicates(configXml);
    checkFileNamesForDuplicates(configXml);
    checkFieldNamesInOneFileForDuplicates(configXml);
    checkReferencesToRules(configXml);
    checkReferencesToFiles(configXml);
    checkThatFilesDontHaveGrandParents(configXml);
  }

  private void checkRuleNamesForDuplicates(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<ValidationRuleXml> rules = configXml.getValidationRules();
    try {
      Maps.uniqueIndex(rules, new Function<ValidationRuleXml, String>() {
        @Override
        public String apply(ValidationRuleXml rule) {
          return rule.getId();
        }
      });
    } catch (IllegalArgumentException e) {
      throw new XmlValidatorException("Uniqueness of rule names is violated (" +
          e.getMessage() + ")");
    }
  }

  private void checkFileNamesForDuplicates(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    try {
      Maps.uniqueIndex(files, new Function<OutputFileXml, String>() {
        @Override
        public String apply(OutputFileXml file) {
          return file.getName();
        }
      });
    } catch (IllegalArgumentException e) {
      throw new XmlValidatorException("Uniqueness of file names is violated (" +
          e.getMessage() + ")");
    }
  }

  private void checkFieldNamesInOneFileForDuplicates(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    for (OutputFileXml file : files) {
      ArrayList<OutputFieldXml> fields = file.getFields();
      if (fields == null) {
        continue;
      }

      try {
        Maps.uniqueIndex(fields, new Function<OutputFieldXml, String>() {
          @Override
          public String apply(OutputFieldXml field) {
            return field.getName();
          }
        });
      } catch (IllegalArgumentException e) {
        throw new XmlValidatorException("Uniqueness of field names for the file " +
            file.getName() + " is violated (" + e.getMessage() + ")");
      }
    }
  }

  private void checkReferencesToRules(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<ValidationRuleXml> rules = configXml.getValidationRules();
    ImmutableMap<String, ValidationRuleXml> ruleNameIndex = Maps.uniqueIndex(rules,
        new Function<ValidationRuleXml, String>() {
          @Override
          public String apply(ValidationRuleXml rule) {
            return rule.getId();
          }
        }); // We're sure that IllegalArgumentException will not arise
            // because checkRuleNamesForDuplicates() is called earlier.

    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    for (OutputFileXml file : files) {
      ArrayList<OutputFieldXml> fields = file.getFields();
      if (fields == null) {
        continue;
      }

      for (OutputFieldXml field : fields) {
        String ruleName = field.getRule();
        if (ruleName != null && !ruleNameIndex.containsKey(ruleName)) {
          throw new XmlValidatorException("The field " + field.getName() +
              " refers to the rule " + ruleName + " which doesn't exist.");
        }
      }
    }
  }

  private void checkReferencesToFiles(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    ImmutableMap<String, OutputFileXml> fileNameIndex = Maps.uniqueIndex(files,
        new Function<OutputFileXml, String>() {
          @Override
          public String apply(OutputFileXml file) {
            return file.getName();
          }
        }); // We're sure that IllegalArgumentException will not arise
            // because checkFileNamesForDuplicates() is called earlier.

    for (OutputFileXml file : files) {
      String fileName = file.getName();
      ExtendsXml extendsFile = file.getExtendsFile();
      if (extendsFile != null) {
        String parentFileName = extendsFile.getFile();

        if (fileName.equals(parentFileName)) {
          throw new XmlValidatorException("The file " + fileName + " refers to itself.");
        }
        if (!fileNameIndex.containsKey(parentFileName)) {
          throw new XmlValidatorException("The file " + fileName +
              " refers to the file " + parentFileName + " which doesn't exist.");
        }
      }
    }
  }

  private void checkThatFilesDontHaveGrandParents(ConstraintConfigXml configXml)
      throws XmlValidatorException {
    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    ImmutableMap<String, OutputFileXml> fileNameIndex = Maps.uniqueIndex(files,
        new Function<OutputFileXml, String>() {
          @Override
          public String apply(OutputFileXml file) {
            return file.getName();
          }
        }); // We're sure that IllegalArgumentException will not arise
            // because checkFileNamesForDuplicates() is called earlier.

    for (OutputFileXml file : files) {
      ExtendsXml extendsFile = file.getExtendsFile();
      if (extendsFile != null) {
        String parentFileName = extendsFile.getFile();
        OutputFileXml parentFile = fileNameIndex.get(parentFileName);

        if (parentFile.getExtendsFile() != null) {
          String grandParentFileName = parentFile.getExtendsFile().getFile();
          throw new XmlValidatorException("The file " + file.getName() +
              " refers to the file " + parentFileName +
              " which, in turn, refers to the file " + grandParentFileName + "." +
              " Such kind of relationships is forbidden.");
        }
      }
    }
  }

}

package by.sazonenka.katana.xml.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.domain.ExtendsXml;
import by.sazonenka.katana.xml.domain.OutputFieldXml;
import by.sazonenka.katana.xml.domain.OutputFileXml;
import by.sazonenka.katana.xml.domain.ValidationRuleXml;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

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
      throw new XmlValidatorException(""); // TODO: provide a message
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
      throw new XmlValidatorException("");
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
        throw new XmlValidatorException("");
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
        });

    ArrayList<OutputFileXml> files = configXml.getOutputFiles();
    for (OutputFileXml file : files) {
      ArrayList<OutputFieldXml> fields = file.getFields();
      if (fields == null) {
        continue;
      }

      for (OutputFieldXml field : fields) {
        String ruleName = field.getRule();
        if (ruleName != null && !ruleNameIndex.containsKey(ruleName)) {
          throw new XmlValidatorException("");
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
        });

    for (OutputFileXml file : files) {
      String fileName = file.getName();
      ExtendsXml parentFile = file.getExtendsFile();
      if (parentFile != null) {
        String parentFileName = parentFile.getFile();
        if (fileName.equals(parentFileName) || !fileNameIndex.containsKey(parentFileName)) {
          throw new XmlValidatorException("");
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
        });

    for (OutputFileXml file : files) {
      ExtendsXml extendsFile = file.getExtendsFile();
      if (extendsFile != null) {
        String parentFileName = extendsFile.getFile();
        OutputFileXml parentFile = fileNameIndex.get(parentFileName);

        if (parentFile.getExtendsFile() != null) {
          throw new XmlValidatorException("");
        }
      }
    }
  }

}

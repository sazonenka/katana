package by.sazonenka.katana.persistence.converter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import by.sazonenka.katana.persistence.converter.XmlConverter;
import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.xml.domain.ConstraintConfigXml;
import by.sazonenka.katana.xml.domain.ExtendsXml;
import by.sazonenka.katana.xml.domain.OutputFieldXml;
import by.sazonenka.katana.xml.domain.OutputFileXml;
import by.sazonenka.katana.xml.domain.ValidationRuleXml;

/**
 * @author Aliaksandr Sazonenka
 */
@Service
@Transactional
public final class XmlConverterImpl implements XmlConverter {

  private final ConstraintConfigDao configDao;
  private final ValidationRuleDao ruleDao;
  private final OutputFileDao fileDao;
  private final OutputFieldDao fieldDao;

  @Inject
  public XmlConverterImpl(ConstraintConfigDao configDao, ValidationRuleDao ruleDao,
      OutputFileDao fileDao, OutputFieldDao fieldDao) {
    this.configDao = Preconditions.checkNotNull(configDao);
    this.ruleDao = Preconditions.checkNotNull(ruleDao);
    this.fileDao = Preconditions.checkNotNull(fileDao);
    this.fieldDao = Preconditions.checkNotNull(fieldDao);
  }

  @Override
  public ConstraintConfigXml loadConfigXml(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    ConstraintConfig config = configDao.get(configId);
    List<ValidationRule> rules = ruleDao.findByConfig(config);
    List<OutputFile> files = fileDao.findByConfig(config);

    Multimap<Long, OutputField> fields = ArrayListMultimap.create();
    for (OutputFile file : files) {
      fields.putAll(file.getId(), fieldDao.findByFile(file));
    }

    return compileToConfigXml(config, rules, files, fields);
  }

  private ConstraintConfigXml compileToConfigXml(ConstraintConfig config,
      List<ValidationRule> rules, List<OutputFile> files,
      final Multimap<Long, OutputField> fields) {
    ConstraintConfigXml configXml = new ConstraintConfigXml();

    configXml.setId(config.getName());

    configXml.setValidationRules(Lists.newArrayList(Lists.transform(rules,
        new Function<ValidationRule, ValidationRuleXml>() {
          @Override
          public ValidationRuleXml apply(ValidationRule rule) {
            ValidationRuleXml ruleXml = new ValidationRuleXml();

            ruleXml.setId(rule.getName());
            ruleXml.setNullable(rule.isNullable());
            ruleXml.setRegexp(rule.getRegexp());
            ruleXml.setDescription(rule.getDescription());

            return ruleXml;
          }
        }
    )));

    configXml.setOutputFiles(Lists.newArrayList(Lists.transform(files,
        new Function<OutputFile, OutputFileXml>() {
          @Override
          public OutputFileXml apply(OutputFile file) {
            OutputFileXml fileXml = new OutputFileXml();

            fileXml.setName(file.getName());
            if (file.getParent() != null) {
              ExtendsXml extendsXml = new ExtendsXml();
              extendsXml.setFile(file.getParent().getName());

              fileXml.setExtendsFile(extendsXml);
            }

            List<OutputField> fieldsForFile = Lists.newArrayList(fields.get(file.getId()));
            fileXml.setFields(Lists.newArrayList(Lists.transform(fieldsForFile,
                new Function<OutputField, OutputFieldXml>() {
                  @Override
                  public OutputFieldXml apply(OutputField field) {
                    OutputFieldXml fieldXml = new OutputFieldXml();

                    fieldXml.setName(field.getName());
                    fieldXml.setRule(field.getRule() != null ? field.getRule().getName() : null);

                    return fieldXml;
                  }
                }
            )));

            return fileXml;
          }
        }
    )));

    return configXml;
  }

  @Override
  public void saveConfigXml(ConstraintConfigXml configXml) {
    Preconditions.checkNotNull(configXml, "Got unexpected null 'configXml' passed to the method.");

    ConstraintConfig config = new ConstraintConfig(configXml.getId(), "", new Date());
    configDao.save(config);

    saveRules(configXml, config);
    saveFiles(configXml, config);
    saveFields(configXml, config);
  }

  private void saveRules(ConstraintConfigXml configXml, ConstraintConfig config) {
    ArrayList<ValidationRuleXml> rulesXml = configXml.getValidationRules();
    if (rulesXml != null) {
      for (int i = 0; i < rulesXml.size(); ++i) {
        ValidationRuleXml ruleXml = rulesXml.get(i);
        ValidationRule rule = new ValidationRule(ruleXml.getId(),
            ruleXml.isNullable(),
            ruleXml.getRegexp(),
            ruleXml.getDescription(),
            i,
            config);

        ruleDao.save(rule);
      }
    }
  }

  private void saveFiles(ConstraintConfigXml configXml, ConstraintConfig config) {
    ArrayList<OutputFileXml> filesXml = configXml.getOutputFiles();
    if (filesXml != null) {
      for (int i = 0; i < filesXml.size(); ++i) {
        OutputFileXml fileXml = filesXml.get(i);

        if (fileXml.getExtendsFile() == null) {
          OutputFile file = new OutputFile(fileXml.getName(),
              i,
              null,
              config);

          fileDao.save(file);
        }
      }

      for (int i = 0; i < filesXml.size(); ++i) {
        OutputFileXml fileXml = filesXml.get(i);
        if (fileXml.getExtendsFile() != null) {
          OutputFile file = new OutputFile(fileXml.getName(),
              i,
              fileDao.findByNameAndConfig(fileXml.getExtendsFile().getFile(), config),
              config);

          fileDao.save(file);
        }
      }
    }
  }

  private void saveFields(ConstraintConfigXml configXml, ConstraintConfig config) {
    ArrayList<OutputFileXml> filesXml = configXml.getOutputFiles();
    if (filesXml != null) {
      for (OutputFileXml fileXml : filesXml) {
        ArrayList<OutputFieldXml> fieldsXml = fileXml.getFields();
        if (fieldsXml != null) {
          for (int i = 0; i < fieldsXml.size(); ++i) {
            OutputFieldXml fieldXml = fieldsXml.get(i);
            ValidationRule rule = fieldXml.getRule() != null
                ? ruleDao.findByNameAndConfig(fieldXml.getRule(), config)
                : null;
            OutputField field = new OutputField(fieldXml.getName(),
                i,
                fileDao.findByNameAndConfig(fileXml.getName(), config),
                rule);

            fieldDao.save(field);
          }
        }
      }
    }
  }

}

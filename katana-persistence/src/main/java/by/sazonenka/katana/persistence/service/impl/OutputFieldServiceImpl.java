package by.sazonenka.katana.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.service.OutputFieldService;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * This class implements business operations with {@link OutputField}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Service
public final class OutputFieldServiceImpl
    extends GenericServiceImpl<OutputField, Long, OutputFieldDao>
    implements OutputFieldService {

  private final ConstraintConfigDao configDao;
  private final ValidationRuleDao ruleDao;
  private final OutputFileDao fileDao;

  @Inject
  public OutputFieldServiceImpl(ConstraintConfigDao configDao, ValidationRuleDao ruleDao,
      OutputFileDao fileDao, OutputFieldDao fieldDao) {
    super(fieldDao);

    this.configDao = Preconditions.checkNotNull(configDao);
    this.ruleDao = Preconditions.checkNotNull(ruleDao);
    this.fileDao = Preconditions.checkNotNull(fileDao);
  }

  @Override
  public OutputField save(OutputField field) {
    Preconditions.checkNotNull(field, "Got unexpected null 'field' passed to the method.");

    if (field.getId() == null) {
      Long fileId = field.getFile().getId();
      OutputFile file = fileDao.get(fileId);

      field.setOrderInFile((int) dao.getCountByFile(file));
    }

    return dao.save(field);
  }

  @Override
  public void delete(Long fieldId) {
    Preconditions.checkNotNull(fieldId, "Got unexpected null 'fieldId' passed to the method.");

    OutputField fieldToDelete = dao.get(fieldId);
    dao.delete(fieldToDelete);

    verifyOrderOfFields(fieldToDelete.getFile());
  }

  private void verifyOrderOfFields(OutputFile file) {
    List<OutputField> allFieldsByFile = dao.findByFile(file);
    for (int i = 0; i < allFieldsByFile.size(); ++i) {
      OutputField field = allFieldsByFile.get(i);
      if (field.getOrderInFile() != i) {
        field.setOrderInFile(i);
        dao.save(field);
      }
    }
  }

  @Override
  public List<OutputField> findByFile(Long fileId) {
    Preconditions.checkNotNull(fileId, "Got unexpected null 'fileId' passed to the method.");

    return dao.findByFile(fileDao.get(fileId));
  }

  @Override
  public List<OutputField> findByRule(Long ruleId) {
    Preconditions.checkNotNull(ruleId, "Got unexpected null 'ruleId' passed to the method.");

    return dao.findByRule(ruleDao.get(ruleId));
  }

  @Override
  public boolean checkFieldNamesForDuplicates(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    List<OutputFile> files = fileDao.findByConfig(configDao.get(configId));
    for (OutputFile file : files) {
      List<OutputField> fields = dao.findByFile(file);
      try {
        Maps.uniqueIndex(fields, new Function<OutputField, String>() {
          @Override
          public String apply(OutputField field) {
            return field.getName();
          }
        });
      } catch (IllegalArgumentException e) {
        return true;
      }
    }
    return false;
  }

}

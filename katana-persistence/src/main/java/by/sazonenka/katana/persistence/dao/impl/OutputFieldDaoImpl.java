package by.sazonenka.katana.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import by.sazonenka.katana.persistence.dao.OutputFieldDao;
import by.sazonenka.katana.persistence.domain.OutputField;
import by.sazonenka.katana.persistence.domain.OutputFile;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.base.Preconditions;

/**
 * This class implements basic CRUD operations with {@link OutputField}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Repository
public final class OutputFieldDaoImpl
    extends GenericDaoImpl<OutputField, Long>
    implements OutputFieldDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<OutputField> findByFile(OutputFile file) {
    Preconditions.checkNotNull(file, "Got unexpected null 'file' passed to the method.");

    return getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFieldsByFile", "file", file);
  }

  @Override
  public long getCountByFile(OutputFile file) {
    Preconditions.checkNotNull(file, "Got unexpected null 'file' passed to the method.");

    return (Long) getHibernateTemplate().findByNamedQueryAndNamedParam(
        "getFieldCountByFile", "file", file).get(0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<OutputField> findByRule(ValidationRule rule) {
    Preconditions.checkNotNull(rule, "Got unexpected null 'rule' passed to the method.");

    return getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFieldsByRule", "rule", rule);
  }

  @SuppressWarnings("unchecked")
  @Override
  public OutputField findByNameAndFile(String name, OutputFile file) {
    Preconditions.checkNotNull(name, "Got unexpected null 'name' passed to the method.");
    Preconditions.checkNotNull(file, "Got unexpected null 'file' passed to the method.");

    List<OutputField> fields = getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFieldsByNameAndFile",
        new String[] { "name", "file" },
        new Object[] { name, file });

    if (fields.isEmpty()) {
      return null;
    } else {
      return fields.get(0);
    }
  }

}

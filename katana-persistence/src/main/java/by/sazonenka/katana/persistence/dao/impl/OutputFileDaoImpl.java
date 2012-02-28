package by.sazonenka.katana.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import by.sazonenka.katana.persistence.dao.OutputFileDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.OutputFile;

import com.google.common.base.Preconditions;

/**
 * This class implements basic CRUD operations with {@link OutputFile}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Repository
public final class OutputFileDaoImpl
    extends GenericDaoImpl<OutputFile, Long>
    implements OutputFileDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<OutputFile> findByConfig(ConstraintConfig config) {
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    return getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFilesByConfig", "config", config);
  }

  @Override
  public long getCountByConfig(ConstraintConfig config) {
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    return (Long) getHibernateTemplate().findByNamedQueryAndNamedParam(
        "getFileCountByConfig", "config", config).get(0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<OutputFile> findByParent(OutputFile parent) {
    Preconditions.checkNotNull(parent, "Got unexpected null 'parent' passed to the method.");

    return getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFilesByParent", "parent", parent);
  }

  @SuppressWarnings("unchecked")
  @Override
  public OutputFile findByNameAndConfig(String name, ConstraintConfig config) {
    Preconditions.checkNotNull(name, "Got unexpected null 'name' passed to the method.");
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    List<OutputFile> files = (List<OutputFile>) getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findFilesByNameAndConfig",
        new String[] { "name", "config" },
        new Object[] { name, config });

    if (files.isEmpty()) {
      return null;
    } else {
      return files.get(0);
    }
  }

}

package by.sazonenka.katana.persistence.dao;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;

/**
 * Interface that specifies a set of operations which can be performed on
 * {@link ConstraintConfig} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericDao
 */
public interface ConstraintConfigDao extends GenericDao<ConstraintConfig, Long> {

  /** Returns a list of all {@link ConstraintConfig}s. */
  List<ConstraintConfig> findAll();

  long getCount();

}

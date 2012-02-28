package by.sazonenka.katana.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;

/**
 * This class implements basic CRUD operations with {@link ConstraintConfig}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Repository
public final class ConstraintConfigDaoImpl
    extends GenericDaoImpl<ConstraintConfig, Long>
    implements ConstraintConfigDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<ConstraintConfig> findAll() {
    return getHibernateTemplate().findByNamedQuery("findAllConfigs");
  }

  @Override
  public long getCount() {
    return (Long) getHibernateTemplate().findByNamedQuery("getConfigCount").get(0);
  }

}

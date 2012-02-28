package by.sazonenka.katana.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.base.Preconditions;

/**
 * This class implements basic CRUD operations with {@link ValidationRule}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Repository
public final class ValidationRuleDaoImpl
    extends GenericDaoImpl<ValidationRule, Long>
    implements ValidationRuleDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<ValidationRule> findByConfig(ConstraintConfig config) {
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    return getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findRulesByConfig", "config", config);
  }

  @Override
  public long getCountByConfig(ConstraintConfig config) {
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    return (Long) getHibernateTemplate().findByNamedQueryAndNamedParam(
        "getRuleCountByConfig", "config", config).get(0);
  }

  @SuppressWarnings("unchecked")
  @Override
  public ValidationRule findByNameAndConfig(String name, ConstraintConfig config) {
    Preconditions.checkNotNull(name, "Got unexpected null 'name' passed to the method.");
    Preconditions.checkNotNull(config, "Got unexpected null 'config' passed to the method.");

    List<ValidationRule> rules = (List<ValidationRule>) getHibernateTemplate().findByNamedQueryAndNamedParam(
        "findRulesByNameAndConfig",
        new String[] { "name", "config" },
        new Object[] { name, config });

    if (rules.isEmpty()) {
      return null;
    } else {
      return rules.get(0);
    }
  }

}

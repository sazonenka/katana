package by.sazonenka.katana.persistence.dao;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * Interface that specifies a set of operations which can be performed on
 * {@link ValidationRule} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericDao
 */
public interface ValidationRuleDao extends GenericDao<ValidationRule, Long> {

  /**
   * Returns a list of all {@link ValidationRule}s
   * associated with the given {@link ConstraintConfig}.
   */
  List<ValidationRule> findByConfig(ConstraintConfig config);

  long getCountByConfig(ConstraintConfig config);

  ValidationRule findByNameAndConfig(String name, ConstraintConfig config);

}

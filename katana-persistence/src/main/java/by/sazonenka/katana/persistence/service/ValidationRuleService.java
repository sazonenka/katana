package by.sazonenka.katana.persistence.service;

import java.util.List;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;

/**
 * Interface that specifies a set of business operations
 * which can be performed on {@link ValidationRule} entity.
 *
 * @author Aliaksandr Sazonenka
 * @see GenericService
 */
public interface ValidationRuleService extends GenericService<ValidationRule, Long> {

  /**
   * Returns a list of all {@link ValidationRule}s
   * associated with a {@link ConstraintConfig} with the given <code>configId</code>.
   */
  List<ValidationRule> findByConfig(Long configId);

  boolean checkRuleNamesForDuplicates(Long configId);

}

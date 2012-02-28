package by.sazonenka.katana.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import by.sazonenka.katana.persistence.dao.ConstraintConfigDao;
import by.sazonenka.katana.persistence.dao.ValidationRuleDao;
import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.persistence.service.ValidationRuleService;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * This class implements business operations with {@link ValidationRule}s.
 *
 * @author Aliaksandr Sazonenka
 */
@Service
public final class ValidationRuleServiceImpl
    extends GenericServiceImpl<ValidationRule, Long, ValidationRuleDao>
    implements ValidationRuleService {

  private final ConstraintConfigDao configDao;

  @Inject
  public ValidationRuleServiceImpl(ConstraintConfigDao configDao, ValidationRuleDao ruleDao) {
    super(ruleDao);

    this.configDao = Preconditions.checkNotNull(configDao);
  }

  @Override
  public ValidationRule save(ValidationRule rule) {
    Preconditions.checkNotNull(rule, "Got unexpected null 'rule' passed to the method.");

    if (rule.getId() == null) {
      Long configId = rule.getConfig().getId();
      ConstraintConfig config = configDao.get(configId);

      rule.setOrderInConfig((int) dao.getCountByConfig(config));
    }

    return dao.save(rule);
  }

  @Override
  public void delete(Long ruleId) {
    Preconditions.checkNotNull(ruleId, "Got unexpected null 'ruleId' passed to the method.");

    ValidationRule ruleToDelete = dao.get(ruleId);
    dao.delete(ruleToDelete);

    verifyOrderOfRules(ruleToDelete.getConfig());
  }

  private void verifyOrderOfRules(ConstraintConfig config) {
    List<ValidationRule> allRulesByConfig = dao.findByConfig(config);
    for (int i = 0; i < allRulesByConfig.size(); ++i) {
      ValidationRule rule = allRulesByConfig.get(i);
      if (rule.getOrderInConfig() != i) {
        rule.setOrderInConfig(i);
        dao.save(rule);
      }
    }
  }

  @Override
  public List<ValidationRule> findByConfig(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    return dao.findByConfig(configDao.get(configId));
  }

  @Override
  public boolean checkRuleNamesForDuplicates(Long configId) {
    Preconditions.checkNotNull(configId, "Got unexpected null 'configId' passed to the method.");

    List<ValidationRule> rules = dao.findByConfig(configDao.get(configId));
    try {
      Maps.uniqueIndex(rules, new Function<ValidationRule, String>() {
        @Override
        public String apply(ValidationRule rule) {
          return rule.getName();
        }
      });
    } catch (IllegalArgumentException e) {
      return true;
    }
    return false;
  }

}

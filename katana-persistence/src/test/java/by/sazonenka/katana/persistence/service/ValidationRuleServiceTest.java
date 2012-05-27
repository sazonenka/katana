package by.sazonenka.katana.persistence.service;

import static by.sazonenka.katana.persistence.service.ServiceTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class ValidationRuleServiceTest extends GenericServiceTest {

  @Test
  public void get() {
    // Given
    ValidationRule rule = getRule1();
    // Expect
    when(ruleDao.get(RULE_1_ID)).thenReturn(rule);
    // Run
    ValidationRule actualRule = ruleService.get(RULE_1_ID);
    // Verify
    verify(ruleDao).get(RULE_1_ID);
    // Assert
    assertThat(actualRule, is(rule));
  }

  @Test
  public void saveNewRule() {
    // Given
    ValidationRule rule = getRule1();
    ConstraintConfig config = rule.getConfig();
    config.setId(CONFIG_1_ID);
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(ruleDao.getCountByConfig(config)).thenReturn(RULES_IN_CONFIG_1);
    when(ruleDao.save(rule)).thenReturn(rule);
    // Run
    ValidationRule savedRule = ruleService.save(rule);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(ruleDao).getCountByConfig(config);
    inOrder.verify(ruleDao).save(rule);
    // Assert
    assertThat(savedRule, is(rule));
    assertThat(savedRule.getOrderInConfig(), is(RULES_IN_CONFIG_1.intValue()));
  }

  @Test
  public void saveExistingRule() {
   // Given
    ValidationRule rule = getRule1();
    rule.setId(RULE_1_ID);
    // Expect
    when(ruleDao.save(rule)).thenReturn(rule);
    // Run
    ValidationRule savedRule = ruleService.save(rule);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao);
    inOrder.verify(configDao, never()).get(any(Long.class));
    inOrder.verify(ruleDao, never()).getCountByConfig(any(ConstraintConfig.class));
    inOrder.verify(ruleDao).save(rule);
    // Assert
    assertThat(savedRule, is(rule));
  }

  @Test
  public void delete() {
    // Given
    ValidationRule ruleToDelete = getRule1();
    ConstraintConfig config = ruleToDelete.getConfig();

    ValidationRule rule1 = getRule1();
    rule1.setOrderInConfig(0);
    ValidationRule rule2 = getRule1();
    rule2.setOrderInConfig(2);
    List<ValidationRule> rules = Lists.newArrayList(rule1, rule2);
    // Expect
    when(ruleDao.get(RULE_1_ID)).thenReturn(ruleToDelete);
    when(ruleDao.findByConfig(config)).thenReturn(rules);
    // Run
    ruleService.delete(RULE_1_ID);
    // Verify
    InOrder inOrder = inOrder(ruleDao);
    inOrder.verify(ruleDao).get(RULE_1_ID);
    inOrder.verify(ruleDao).delete(ruleToDelete);

    inOrder.verify(ruleDao).findByConfig(config);
    inOrder.verify(ruleDao).save(rule2);
    // Assert
    assertThat(rule2.getOrderInConfig(), is(1));
  }

  @Test
  public void findByConfig() {
    // Given
    ConstraintConfig config = getConfig1();
    List<ValidationRule> rules = Lists.newArrayList(getRule1(), getRule1(), getRule1());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(ruleDao.findByConfig(config)).thenReturn(rules);
    // Run
    List<ValidationRule> actualRules = ruleService.findByConfig(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(ruleDao).findByConfig(config);
    // Assert
    assertThat(actualRules, is(rules));
  }

  @Test
  public void nameDuplicatesFound() {
    // Given
    ConstraintConfig config = getConfig1();
    List<ValidationRule> rules = Lists.newArrayList(getRule1(), getRule1());
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(ruleDao.findByConfig(config)).thenReturn(rules);
    // Run
    boolean duplicatesFound = ruleService.checkRuleNamesForDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(ruleDao).findByConfig(config);
    // Assert
    assertThat(duplicatesFound, is(true));
  }

  @Test
  public void nameDuplicatesNotFound() {
    // Given
    ConstraintConfig config = getConfig1();

    ValidationRule rule1 = getRule1();
    ValidationRule rule2 = getRule1();
    rule2.setName(rule2.getName() + " ");
    List<ValidationRule> rules = Lists.newArrayList(rule1, rule2);
    // Expect
    when(configDao.get(CONFIG_1_ID)).thenReturn(config);
    when(ruleDao.findByConfig(config)).thenReturn(rules);
    // Run
    boolean duplicatesFound = ruleService.checkRuleNamesForDuplicates(CONFIG_1_ID);
    // Verify
    InOrder inOrder = inOrder(configDao, ruleDao);
    inOrder.verify(configDao).get(CONFIG_1_ID);
    inOrder.verify(ruleDao).findByConfig(config);
    // Assert
    assertThat(duplicatesFound, is(false));
  }

}

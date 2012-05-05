package by.sazonenka.katana.web.server;

import static by.sazonenka.katana.web.server.ManagerTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;

import by.sazonenka.katana.persistence.domain.ValidationRule;
import by.sazonenka.katana.web.model.ValidationRuleModel;

/**
 * @author Aliaksandr Sazonenka
 */
public class ValidationRuleManagerTest extends GenericManagerTest {

  @Test
  public void findByConfig() throws Exception {
    // Given
    List<ValidationRule> expectedRules = Lists.newArrayList(getRule1(), getRule2());
    List<ValidationRuleModel> expectedRuleModels = Lists.newArrayList(getRuleModel1(),
        getRuleModel2());
    // Expect
    when(ruleService.findByConfig(CONFIG_1_ID)).thenReturn(expectedRules);
    // Run
    List<ValidationRuleModel> actualRuleModels = ruleManager.findByConfig(CONFIG_1_ID);
    // Verify
    verify(ruleService).findByConfig(CONFIG_1_ID);
    // Assert
    assertThat(actualRuleModels, is(expectedRuleModels));
  }

  @Test
  public void saveNewRule() throws Exception {
    // Given
    ValidationRule newRule = getNewRule1();
    ValidationRule savedRule = getRule1();

    ValidationRuleModel newRuleModel = getNewRuleModel1();
    ValidationRuleModel expectedRuleModel = getRuleModel1();
    // Expect
    when(ruleService.save(newRule)).thenReturn(savedRule);
    // Run
    ValidationRuleModel actualRuleModel = ruleManager.save(newRuleModel);
    // Verify
    verify(ruleService).save(newRule);
    // Assert
    assertThat(actualRuleModel, is(expectedRuleModel));
  }

  @Test
  public void updateRule() throws Exception {
    // Given
    ValidationRule rule = getRule1();

    ValidationRuleModel ruleModel = getRuleModel1();
    String expectedRuleName = ruleModel.getName() + "_updated";
    ruleModel.setName(expectedRuleName);
    // Expect
    when(ruleService.get(RULE_1_ID)).thenReturn(rule);
    when(ruleService.save(rule)).thenReturn(rule);
    // Run
    ValidationRuleModel actualRuleModel = ruleManager.save(ruleModel);
    // Verify
    verify(ruleService).get(RULE_1_ID);
    verify(ruleService).save(rule);
    // Assert
    assertThat(actualRuleModel, is(ruleModel));
    assertThat(actualRuleModel.getName(), is(expectedRuleName));
  }

  @Test
  public void swap() throws Exception {
    // Given
    ValidationRule rule1 = getRule1();
    int rule1Order = rule1.getOrderInConfig();
    ValidationRule rule2 = getRule2();
    int rule2Order = rule2.getOrderInConfig();
    // Expect
    when(ruleService.get(RULE_1_ID)).thenReturn(rule1);
    when(ruleService.get(RULE_2_ID)).thenReturn(rule2);
    // Run
    ruleManager.swap(RULE_1_ID, RULE_2_ID);
    // Verify
    ArgumentCaptor<ValidationRule> captor = ArgumentCaptor.forClass(ValidationRule.class);
    verify(ruleService, times(2)).save(captor.capture());
    // Assert
    List<ValidationRule> savedRules = captor.getAllValues();
    assertThat(savedRules.get(0).getId(), is(RULE_1_ID));
    assertThat(savedRules.get(0).getOrderInConfig(), is(rule2Order));
    assertThat(savedRules.get(1).getId(), is(RULE_2_ID));
    assertThat(savedRules.get(1).getOrderInConfig(), is(rule1Order));
  }

  @Test
  public void delete() throws Exception {
    // Run
    ruleManager.delete(RULE_1_ID);
    // Verify
    verify(ruleService).delete(RULE_1_ID);
  }

}

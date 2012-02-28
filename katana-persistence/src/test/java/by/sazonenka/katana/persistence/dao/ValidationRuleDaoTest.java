package by.sazonenka.katana.persistence.dao;

import static by.sazonenka.katana.persistence.dao.DaoTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import by.sazonenka.katana.persistence.domain.ConstraintConfig;
import by.sazonenka.katana.persistence.domain.ValidationRule;

import com.google.common.collect.Lists;

/**
 * @author Aliaksandr Sazonenka
 */
public class ValidationRuleDaoTest extends GenericDaoTest {

  @Test
  public void findRulesByConfigSize() {
    int expectedRuleCount = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();

    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    List<ValidationRule> rulesByConfig = ruleDao.findByConfig(config);

    assertThat("Got unexpected null while executing the query for ValidationRules fetching.",
        rulesByConfig, notNullValue());
    assertThat("Got unexpected size of a list while executing the query "
        + "for ValidationRules fetching.",
        rulesByConfig.size(), is(expectedRuleCount));
  }

  @Test
  public void findRulesByConfigSortedByOrder() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    List<ValidationRule> rulesByConfig = ruleDao.findByConfig(config);

    List<ValidationRule> sortedRules = Lists.newArrayList(rulesByConfig);
    Collections.sort(sortedRules, new Comparator<ValidationRule>() {
      @Override
      public int compare(ValidationRule r1, ValidationRule r2) {
        return r1.getOrderInConfig() - r2.getOrderInConfig();
      }
    });

    assertThat("Got unexpected sorting while executing the query for ValidationRules fetching.",
        rulesByConfig, is(sortedRules));
  }

  @Test
  public void getCountByConfig() {
    int expectedRuleCount = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();

    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    long count = ruleDao.getCountByConfig(config);

    assertThat("Got unexpected count of ValidationRules.",
        count, is((long) expectedRuleCount));
  }

  @Test
  public void findRuleByNameAndConfig() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);

    ValidationRule existingRule = ruleDao.findByNameAndConfig(RULE_EXIST_NAME, config);

    assertThat("Got unexpected null while executing the query for ValidationRule fetching.",
        existingRule, notNullValue());
    assertThat("Got unexpected id of a ValidationRule found by name.",
        existingRule.getId(), is(RULE_EXIST_ID));

    ValidationRule nonExistingRule = ruleDao.findByNameAndConfig(RULE_NONEXIST_NAME, config);

    assertThat("Got unexpected not-null while executing the query for ValidationRule fetching.",
        nonExistingRule, nullValue());
  }

  @Test
  public void getExistingRule() {
    ValidationRule existingRule = ruleDao.get(RULE_EXIST_ID);

    assertThat("Got unexpected null while loading the existing instance.",
        existingRule, notNullValue());
    assertThat("Got unexpected 'id' value while loading the existing instance.",
        existingRule.getId(), is(RULE_EXIST_ID));
    assertThat("Got unexpected 'name' value while loading the existing instance.",
        existingRule.getName(), is(RULE_EXIST_NAME));
    assertThat("Got unexpected 'nullable' value while loading the existing instance.",
        existingRule.isNullable(), is(RULE_EXIST_NULLABLE));
    assertThat("Got unexpected 'regexp' value while loading the existing instance.",
        existingRule.getRegexp(), is(RULE_EXIST_REGEXP));
    assertThat("Got unexpected 'description' value while loading the existing instance.",
        existingRule.getDescription(), is(RULE_EXIST_DESCRIPTION));
    assertThat("Got unexpected 'orderInConfig' value while loading the existing instance.",
        existingRule.getOrderInConfig(), is(RULE_EXIST_ORDER));
    assertThat("Got unexpected 'config.id' value while loading the existing instance.",
        existingRule.getConfig().getId(), is(CONFIG_EXIST_ID));
  }

  @Test
  public void getNonExistingRule() {
    ValidationRule nonExistingRule = ruleDao.get(RULE_NONEXIST_ID);

    assertThat("Got unexpected not-null value while loading a non-existing instance.",
        nonExistingRule, nullValue());
  }

  @Test
  public void insertRule() {
    ConstraintConfig config = get(DomainMapping.CONSTRAINT_CONFIG, CONFIG_EXIST_ID);
    ValidationRule ruleToInsert = new ValidationRule(RULE_NONEXIST_NAME, RULE_NONEXIST_NULLABLE,
        RULE_NONEXIST_REGEXP, RULE_NONEXIST_DESCRIPTION, RULE_NONEXIST_ORDER, config);

    int sizeBeforeInsert = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();
    ruleDao.save(ruleToInsert);
    ruleDao.flush();
    int sizeAfterInsert = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to increment by one after an insert operation.",
        sizeAfterInsert, is(sizeBeforeInsert + 1));

    ValidationRule insertedRule = get(DomainMapping.VALIDATION_RULE, ruleToInsert.getId());

    assertThat("Got unexpected null while loading the inserted instance.",
        insertedRule, notNullValue());
    assertThat("Got unexpected 'name' value while loading the inserted instance.",
        insertedRule.getName(), is(RULE_NONEXIST_NAME));
    assertThat("Got unexpected 'nullable' value while loading the inserted instance.",
        insertedRule.isNullable(), is(RULE_NONEXIST_NULLABLE));
    assertThat("Got unexpected 'regexp' value while loading the inserted instance.",
        insertedRule.getRegexp(), is(RULE_NONEXIST_REGEXP));
    assertThat("Got unexpected 'description' value while loading the inserted instance.",
        insertedRule.getDescription(), is(RULE_NONEXIST_DESCRIPTION));
    assertThat("Got unexpected 'orderInConfig' value while loading the inserted instance.",
        insertedRule.getOrderInConfig(), is(RULE_NONEXIST_ORDER));
    assertThat("Got unexpected 'config.id' value while loading the inserted instance.",
        insertedRule.getConfig().getId(), is(CONFIG_EXIST_ID));
  }

  @Test
  public void updateRule() {
    ValidationRule ruleToUpdate = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);
    ruleToUpdate.setName(RULE_NONEXIST_NAME);
    ruleToUpdate.setNullable(RULE_NONEXIST_NULLABLE);
    ruleToUpdate.setRegexp(RULE_NONEXIST_REGEXP);
    ruleToUpdate.setDescription(RULE_NONEXIST_DESCRIPTION);
    ruleToUpdate.setOrderInConfig(RULE_NONEXIST_ORDER);

    int sizeBeforeUpdate = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();
    ruleDao.save(ruleToUpdate);
    ruleDao.flush();
    int sizeAfterUpdate = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to remain the same after an update operation.",
        sizeAfterUpdate, is(sizeBeforeUpdate));

    ValidationRule updatedRule = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);

    assertThat("Got unexpected null while loading the updated instance.",
        updatedRule, notNullValue());
    assertThat("Got unexpected 'name' value while loading the updated instance.",
        updatedRule.getName(), is(RULE_NONEXIST_NAME));
    assertThat("Got unexpected 'nullable' value while loading the updated instance.",
        updatedRule.isNullable(), is(RULE_NONEXIST_NULLABLE));
    assertThat("Got unexpected 'regexp' value while loading the updated instance.",
        updatedRule.getRegexp(), is(RULE_NONEXIST_REGEXP));
    assertThat("Got unexpected 'description' value while loading the updated instance.",
        updatedRule.getDescription(), is(RULE_NONEXIST_DESCRIPTION));
    assertThat("Got unexpected 'orderInConfig' value while loading the updated instance.",
        updatedRule.getOrderInConfig(), is(RULE_NONEXIST_ORDER));
  }

  @Test
  public void incrementVersion() {
    ValidationRule ruleToUpdate = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);
    ruleToUpdate.setName(RULE_NONEXIST_NAME);

    Integer versionBeforeUpdate = ruleToUpdate.getVersion();

    ruleDao.save(ruleToUpdate);
    ruleDao.flush();

    ValidationRule updatedRule = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);
    Integer versionAfterUpdate = updatedRule.getVersion();

    assertThat("A version of persistent instances is supposed "
        + "to increment by one after an update operation.",
        versionAfterUpdate, is(versionBeforeUpdate + 1));
  }

  @Test
  public void deleteRule() {
    ValidationRule ruleToDelete = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);

    int sizeBeforeDelete = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();
    ruleDao.delete(ruleToDelete);
    ruleDao.flush();
    int sizeAfterDelete = findByConfig(DomainMapping.VALIDATION_RULE, CONFIG_EXIST_ID).size();

    assertThat("A number of persistent instances is supposed "
        + "to decrement by one after a delete operation.",
        sizeAfterDelete, is(sizeBeforeDelete - 1));

    ValidationRule deletedRule = get(DomainMapping.VALIDATION_RULE, RULE_EXIST_ID);

    assertThat("Got unexpected not-null value while loading the deleted instance.",
        deletedRule, nullValue());
  }

}

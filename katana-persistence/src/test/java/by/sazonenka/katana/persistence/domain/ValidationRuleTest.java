package by.sazonenka.katana.persistence.domain;

import static by.sazonenka.katana.persistence.domain.DomainTestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Aliaksandr Sazonenka
 */
public class ValidationRuleTest extends GenericDomainTest<ValidationRule> {

  @Override
  protected void initModels() {
    model = new ValidationRule(NAME_1, NULLABLE_1, REGEXP_1, DESCRIPTION_1, ORDER_1, null);
    modelEqual = new ValidationRule(NAME_1, NULLABLE_1, REGEXP_1, DESCRIPTION_1, ORDER_1, null);
    modelNotEqual = new ValidationRule(NAME_2, NULLABLE_2, REGEXP_2, DESCRIPTION_2, ORDER_2, null);
  }

  @Override
  protected void updateModelNotEqual() {
    modelNotEqual.setName(NAME_1);
    modelNotEqual.setNullable(NULLABLE_1);
    modelNotEqual.setRegexp(REGEXP_1);
    modelNotEqual.setDescription(DESCRIPTION_1);
    modelNotEqual.setOrderInConfig(ORDER_1);
  }

  @Override
  protected void checkBasicProperties(ValidationRule rule) {
    assertThat("'name' property hasn't been set correctly.",
        rule.getName(), is(NAME_1));
    assertThat("'nullable' property hasn't been set correctly.",
        rule.isNullable(), is(NULLABLE_1));
    assertThat("'regexp' property hasn't been set correctly.",
        rule.getRegexp(), is(REGEXP_1));
    assertThat("'description' property hasn't been set correctly.",
        rule.getDescription(), is(DESCRIPTION_1));
    assertThat("'orderInConfig' property hasn't been set correctly.",
        rule.getOrderInConfig(), is(ORDER_1));
  }

  @Override
  protected void checkAssociationSetters() {
    ConstraintConfig config = new ConstraintConfig(NAME_1, AUTHOR_1, MODIFIED_1);

    model.setConfig(config);

    assertThat("'config' property hasn't been set correctly.",
        model.getConfig(), is(config));
  }

}

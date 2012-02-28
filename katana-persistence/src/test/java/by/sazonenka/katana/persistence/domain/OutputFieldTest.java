package by.sazonenka.katana.persistence.domain;

import static by.sazonenka.katana.persistence.domain.DomainTestData.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFieldTest extends GenericDomainTest<OutputField> {

  @Override
  protected void initModels() {
    model = new OutputField(NAME_1, ORDER_1, null, null);
    modelEqual = new OutputField(NAME_1, ORDER_1, null, null);
    modelNotEqual = new OutputField(NAME_2, ORDER_2, null, null);
  }

  @Override
  protected void updateModelNotEqual() {
    modelNotEqual.setName(NAME_1);
    modelNotEqual.setOrderInFile(ORDER_1);
  }

  @Override
  protected void checkBasicProperties(OutputField field) {
    assertThat("'name' property hasn't been set correctly.",
        field.getName(), is(NAME_1));
    assertThat("'orderInFile' property hasn't been set correctly.",
        field.getOrderInFile(), is(ORDER_1));
  }

  @Override
  protected void checkAssociationSetters() {
    OutputFile file = new OutputFile(NAME_1, ORDER_1, null, null);
    ValidationRule rule = new ValidationRule(NAME_1, NULLABLE_1, REGEXP_1, DESCRIPTION_1, ORDER_1,
        null);

    model.setFile(file);
    model.setRule(rule);

    assertThat("'file' property hasn't been set correctly.",
        model.getFile(), is(file));
    assertThat("'rule' property hasn't been set correctly.",
        model.getRule(), is(rule));
  }

}

package by.sazonenka.katana.persistence.domain;

import static by.sazonenka.katana.persistence.domain.DomainTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Aliaksandr Sazonenka
 */
public class OutputFileTest extends GenericDomainTest<OutputFile> {

  @Override
  protected void initModels() {
    model = new OutputFile(NAME_1, ORDER_1, null, null);
    modelEqual = new OutputFile(NAME_1, ORDER_1, null, null);
    modelNotEqual = new OutputFile(NAME_2, ORDER_2, null, null);
  }

  @Override
  protected void updateModelNotEqual() {
    modelNotEqual.setName(NAME_1);
    modelNotEqual.setOrderInConfig(ORDER_1);
  }

  @Override
  protected void checkBasicProperties(OutputFile file) {
    assertThat("'name' property hasn't been set correctly.",
        file.getName(), is(NAME_1));
    assertThat("'orderInConfig' property hasn't been set correctly.",
        file.getOrderInConfig(), is(ORDER_1));
  }

  @Override
  protected void checkAssociationSetters() {
    ConstraintConfig config = new ConstraintConfig(NAME_1, AUTHOR_1, MODIFIED_1);
    OutputFile parentFile = new OutputFile(NAME_1, ORDER_1, null, null);

    model.setConfig(config);
    model.setParent(parentFile);

    assertThat("'config' property hasn't been set correctly.",
        model.getConfig(), is(config));
    assertThat("'parent' property hasn't been set correctly.",
        model.getParent(), is(parentFile));
  }

}

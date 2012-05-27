package by.sazonenka.katana.persistence.domain;

import static by.sazonenka.katana.persistence.domain.DomainTestData.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

/**
 * @author Aliaksandr Sazonenka
 */
public class ConstraintConfigTest extends GenericDomainTest<ConstraintConfig> {

  @Override
  protected void initModels() {
    model = new ConstraintConfig(NAME_1, AUTHOR_1, MODIFIED_1);
    modelEqual = new ConstraintConfig(NAME_1, AUTHOR_1, MODIFIED_1);
    modelNotEqual = new ConstraintConfig(NAME_2, AUTHOR_2, MODIFIED_2);
  }

  @Override
  protected void updateModelNotEqual() {
    modelNotEqual.setName(NAME_1);
    modelNotEqual.setAuthor(AUTHOR_1);
    modelNotEqual.setModified(MODIFIED_1);
  }

  @Override
  protected void checkBasicProperties(ConstraintConfig config) {
    assertThat("'name' property hasn't been set correctly.",
        config.getName(), is(NAME_1));
    assertThat("'author' property hasn't been set correctly.",
        config.getAuthor(), is(AUTHOR_1));
    assertThat("'modified' property hasn't been set correctly.",
        config.getModified(), is(MODIFIED_1));
  }

  @Override
  protected void checkAssociationSetters() { /* ConstraintConfig has no association setters. */ }

  @Test
  public void modifiedIsCopiedDefensively() {
    ConstraintConfig config = new ConstraintConfig(NAME_1, AUTHOR_1, MODIFIED_1);
    Date copyOne = config.getModified();
    Date copyTwo = config.getModified();

    assertThat("Getter for the 'modified' property should not ever return null.",
        copyOne, notNullValue());
    assertThat("Getter for the 'modified' property should not ever return null.",
        copyTwo, notNullValue());

    assertThat("Getter for the 'modified' property should defensively copy the date.",
        copyTwo, is(copyOne));
    assertThat("Getter for the 'modified' property should defensively copy the date.",
        copyTwo, not(sameInstance(copyOne)));
  }

}

package by.sazonenka.katana.persistence.domain;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Aliaksandr Sazonenka
 */
public abstract class GenericDomainTest<M extends GenericDomain> {

  protected M model;
  protected M modelEqual;
  protected M modelNotEqual;

  @Before
  public void setUp() {
    initModels();
  }

  protected abstract void initModels();

  @Test
  public void constructorIsNotBroken() {
    checkBasicProperties(modelEqual);
  }

  @Test
  public void basicSettersAreNotBroken() {
    updateModelNotEqual();
    checkBasicProperties(modelNotEqual);
  }

  protected abstract void updateModelNotEqual();
  protected abstract void checkBasicProperties(M modelToCheck);

  @Test
  public void associationSettersAreNotBroken() {
    checkAssociationSetters();
  }

  protected abstract void checkAssociationSetters();

  @Test
  public void equalsIsNotBroken() {
    assertThat("Current implementation of the equals() method is not reflective.\n"
        + "x.equals(x) must return true.",
        model, is(model));
    assertThat("Entities with equal domain attributes should be considered equal.",
        model, is(modelEqual));
    assertThat("Entities with different domain attributes should not be considered equal.",
        model, not(modelNotEqual));
    assertThat("Current implementation of the equals() method is not symmetric.\n"
        + "x.equals(y) must return true if and only if y.equals(x) returns true.",
        modelEqual, is(model));
    assertThat("Current implementation of the equals() method doesn't do instanceof test.",
        new Object(), not((Object) model));
    assertFalse("model.equals(null) must return false.",
        model.equals((Object) null));
  }

  @Test
  public void hashCodeIsNotBroken() {
    assertThat("Whenever it is invoked on the same object, "
        + "the hashCode method must consistently return the same integer.",
        model.hashCode(), is(model.hashCode()));
    assertThat("If two objects are equal, "
        + "the hashCode method on each of the two objects must produce the same integer result.",
        model.hashCode(), is(modelEqual.hashCode()));
  }

  @Test
  public void toStringIsNotBroken() {
    assertThat("String representation of the model should not be null.",
        model, hasToString(notNullValue(String.class)));
    assertThat("String representation of the model should not be empty.",
        model, hasToString(not("")));
  }

}

package by.sazonenka.katana.xml.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import com.google.common.base.Objects;

/**
 * Represents a model of an output field in <code>test_fields_upon_constraints.xml</code> file.
 * <p>
 * The <b>field</b> tag defines a mapping between the field and the validation rule.
 * <ol>
 *  <li>The <b>name</b> attribute specifies the name of the column in the output csv file.
 *  <li>The <b>validation-rule</b> attribute contains the id of existing validation rule.
 * </ol>
 *
 * @author Aliaksandr Sazonenka
 * @see OutputFileXml
 * @see ValidationRuleXml
 */
@Root(name = "field")
public final class OutputFieldXml {

  @Attribute
  private String name;

  @Attribute(name = "validation-rule", required = false)
  private String rule;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OutputFieldXml that = (OutputFieldXml) obj;
    return Objects.equal(name, that.name)
        && Objects.equal(rule, that.rule);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, rule);
  }

  /** Returns the value of the <b>name</b> attribute. */
  public String getName() { return name; }
  /** Sets the value of the <b>name</b> attribute. */
  public void setName(String name) { this.name = name; }

  /** Returns the value of the <b>validation-rule</b> attribute. */
  public String getRule() { return rule; }
  /** Sets the value of the <b>validation-rule</b> attribute. */
  public void setRule(String rule) { this.rule = rule; }

}

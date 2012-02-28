package by.sazonenka.katana.xml.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.google.common.base.Objects;

/**
 * Represents a model of a validation rule in <code>test_fields_upon_constraints.xml</code> file.
 * <p>
 * The <b>validation-rule</b> tag specifies a data constraint
 * that can be applied to multiple fields.
 * <ol>
 *  <li>The validation rule should have a unique identifier (<b>id</b> attribute).
 *  <li>The <b>nullable</b> element specifies if the field can contain <code>null</code> values.
 *  <li>The <b>regexp</b> element defines a regular expression to apply during validation.
 *  <li>The <b>description</b> element provides detailed explanation of the rule.
 * </ol>
 *
 * @author Aliaksandr Sazonenka
 * @see ConstraintConfigXml
 */
@Root(name = "validation-rule")
public final class ValidationRuleXml {

  @Attribute
  private String id;

  @Element
  private boolean nullable;

  @Element
  private String regexp;

  @Element(required = false)
  private String description;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ValidationRuleXml that = (ValidationRuleXml) obj;
    return Objects.equal(id, that.id)
        && Objects.equal(nullable, that.nullable)
        && Objects.equal(regexp, that.regexp)
        && Objects.equal(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, nullable, regexp, description);
  }

  /** Returns the value of the <b>id</b> attribute. */
  public String getId() { return id; }
  /** Sets the value of the <b>id</b> attribute. */
  public void setId(String id) { this.id = id; }

  /** Returns the value of the <b>nullable</b> element. */
  public boolean isNullable() { return nullable; }
  /** Sets the value of the <b>nullable</b> element. */
  public void setNullable(boolean nullable) { this.nullable = nullable; }

  /** Returns the value of the <b>regexp</b> element. */
  public String getRegexp() { return regexp; }
  /** Sets the value of the <b>regexp</b> element. */
  public void setRegexp(String regexp) { this.regexp = regexp; }

  /** Returns the value of the <b>description</b> element. */
  public String getDescription() { return description; }
  /** Sets the value of the <b>description</b> element. */
  public void setDescription(String description) { this.description = description; }

}

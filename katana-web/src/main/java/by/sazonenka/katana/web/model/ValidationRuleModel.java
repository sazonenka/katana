package by.sazonenka.katana.web.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.common.base.Objects;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ValidationRuleModel extends BaseModel {

  private static final long serialVersionUID = 7266328473149080510L;

  public enum Attributes {
    ID("id", "ID"),
    NAME("name", "Name"),
    NULLABLE("nullable", "Nullable"),
    REGEXP("regexp", "Regular Expression"),
    DESCRIPTION("description", "Description"),
    CONFIG("config", "Config");

    private final String id;
    private final String title;

    private Attributes(String id, String title) {
      this.id = id;
      this.title = title;
    }

    public String id() { return id; }
    public String title() { return title; }

  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ValidationRuleModel that = (ValidationRuleModel) obj;
    return Objects.equal(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("id", getId())
            .add("name", getName())
            .add("nullable", getNullable())
            .add("regexp", getRegexp())
            .add("description", getDescription())
            .add("config", getConfig())
            .toString();
  }

  /** Returns the identifier of the rule. */
  public Long getId() { return get(Attributes.ID.id()); }
  /** Sets the identifier of the rule. */
  public void setId(Long id) { set(Attributes.ID.id(), id); }

  /** Returns the name of the rule. */
  public String getName() { return get(Attributes.NAME.id()); }
  /** Sets the name of the rule. */
  public void setName(String name) { set(Attributes.NAME.id(), name); }

  /** Returns the <code>nullable</code> property of the rule. */
  public Boolean getNullable() { return get(Attributes.NULLABLE.id()); }
  /** Sets the <code>nullable</code> property of the rule. */
  public void setNullable(Boolean nullable) { set(Attributes.NULLABLE.id(), nullable); }

  /** Returns the <code>regexp</code> property of the rule. */
  public String getRegexp() { return get(Attributes.REGEXP.id()); }
  /** Sets the <code>regexp</code> property of the rule. */
  public void setRegexp(String regexp) { set(Attributes.REGEXP.id(), regexp); }

  /** Returns the description of the rule. */
  public String getDescription() { return get(Attributes.DESCRIPTION.id()); }
  /** Sets the description of the rule. */
  public void setDescription(String description) { set(Attributes.DESCRIPTION.id(), description); }

  /** Returns the id of the dataset this rule is associated with. */
  public Long getConfig() { return get(Attributes.CONFIG.id()); }
  /** Associates the current rule with a dataset with the given id. */
  public void setConfig(Long config) { set(Attributes.CONFIG.id(), config); }

}

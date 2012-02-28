package by.sazonenka.katana.web.model;

import com.google.common.base.Objects;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFieldModel extends FilesTreeModel {

  private static final long serialVersionUID = 305431040470771415L;

  public enum Attributes {
    FILE("file", "File"),
    RULE("rule", "Rule");

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
    OutputFieldModel that = (OutputFieldModel) obj;
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
            .add("file", getFile())
            .add("rule", getRule())
            .toString();
  }

  /** Returns the id of the file this field is associated with. */
  public Long getFile() { return get(Attributes.FILE.id()); }
  /** Sets the id of the file this field is associated with. */
  public void setFile(Long file) { set(Attributes.FILE.id(), file); }

  /** Returns the name of the rule this field is associated with. */
  public String getRule() { return get(Attributes.RULE.id()); }
  /** Sets the name of the rule this field is associated with. */
  public void setRule(String rule) { set(Attributes.RULE.id(), rule); }

  @Override
  public Type getType() { return Type.FIELD; }

}

package by.sazonenka.katana.web.model;

import com.google.common.base.Objects;

/**
 * @author Aliaksandr Sazonenka
 */
public final class OutputFileModel extends FilesTreeModel {

  private static final long serialVersionUID = 2019137239373657258L;

  public enum Attributes {
    PARENT_FILE("parentFile", "Parent File"),
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
    OutputFileModel that = (OutputFileModel) obj;
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
            .add("parentFile", getParentFile())
            .add("config", getConfig())
            .toString();
  }

  /** Returns the name of the parent file this file extends. */
  public String getParentFile() { return get(Attributes.PARENT_FILE.id()); }
  /** Sets the name of the parent file this file extends. */
  public void setParentFile(String parentFile) { set(Attributes.PARENT_FILE.id(), parentFile); }

  /** Returns the id of the dataset this file is associated with. */
  public Long getConfig() { return get(Attributes.CONFIG.id()); }
  /** Associates the current file with a dataset with the given id. */
  public void setConfig(Long config) { set(Attributes.CONFIG.id(), config); }

  @Override
  public Type getType() { return Type.FILE; }

}

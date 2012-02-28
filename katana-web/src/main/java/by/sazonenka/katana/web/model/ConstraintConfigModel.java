package by.sazonenka.katana.web.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.common.base.Objects;

/**
 * @author Aliaksandr Sazonenka
 */
public final class ConstraintConfigModel extends BaseModel {

  private static final long serialVersionUID = 4809744257871396542L;

  public enum Attributes {
    ID("id", "ID"),
    NAME("name", "Name"),
    AUTHOR("author", "Author"),
    MODIFIED("modified", "Modified");

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
    ConstraintConfigModel that = (ConstraintConfigModel) obj;
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
            .add("author", getAuthor())
            .add("modified", getModified())
            .toString();
  }

  /** Returns the identifier of the dataset. */
  public Long getId() { return get(Attributes.ID.id()); }
  /** Sets the identifier of the dataset. */
  public void setId(Long id) { set(Attributes.ID.id(), id); }

  /** Returns the name of the dataset. */
  public String getName() { return get(Attributes.NAME.id()); }
  /** Sets the name of the dataset. */
  public void setName(String name) { set(Attributes.NAME.id(), name); }

  /** Returns the name of the dataset's author. */
  public String getAuthor() { return get(Attributes.AUTHOR.id()); }
  /** Sets the name of the dataset's author. */
  public void setAuthor(String author) { set(Attributes.AUTHOR.id(), author); }

  /** Returns the timestamp of the config's last modification. */
  public Date getModified() { return get(Attributes.MODIFIED.id()); }
  /** Sets the timestamp of the config's last modification. */
  public void setModified(Date modified) { set(Attributes.MODIFIED.id(), modified); }

}

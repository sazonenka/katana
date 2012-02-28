package by.sazonenka.katana.web.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * @author Aliaksandr Sazonenka
 */
public abstract class FilesTreeModel extends BaseModelData {

  private static final long serialVersionUID = -1361806068336989252L;

  public enum Type { FILE, FIELD }

  public enum Attributes {
    ID("id", "ID"),
    NAME("name", "Name");

    private final String id;
    private final String title;

    private Attributes(String id, String title) {
      this.id = id;
      this.title = title;
    }

    public String id() { return id; }
    public String title() { return title; }

  }

  /** Returns the identifier of the file. */
  public Long getId() { return get(Attributes.ID.id()); }
  /** Sets the identifier of the file. */
  public void setId(Long id) { set(Attributes.ID.id(), id); }

  /** Returns the name of the file. */
  public String getName() { return get(Attributes.NAME.id()); }
  /** Sets the name of the file. */
  public void setName(String name) { set(Attributes.NAME.id(), name); }

  public abstract Type getType();

  public static class UnsupportedFilesTreeModelType extends RuntimeException {

    private static final long serialVersionUID = 520960607570356627L;

    public UnsupportedFilesTreeModelType() { }

    public UnsupportedFilesTreeModelType(String message) {
      super(message);
    }

    public UnsupportedFilesTreeModelType(String message, Throwable cause) {
      super(message, cause);
    }

  }

}

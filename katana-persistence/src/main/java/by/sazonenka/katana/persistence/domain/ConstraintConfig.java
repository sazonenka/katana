package by.sazonenka.katana.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.Objects;

/**
 * Represents a model of a record in the <code>"CONSTRAINT_CONFIG"</code> table.
 *
 * <p>The <code>"CONSTRAINT_CONFIG"</code> table contains the following columns:
 * <ul>
 * <li><code>ID</code>: The surrogate identifier of the config.
 * <li><code>NAME</code>: The name of the dataset.
 * <li><code>AUTHOR</code>: The name of the dataset's author.
 * <li><code>MODIFIED</code>: The timestamp of the config's last modification.
 * <li><code>VERSION</code>: The utility integer used in optimistic locking.
 * </ul>
 *
 * <p>While using the {@link ConstraintConfig} class, a client
 * should consider the following caveats:
 * <ul>
 * <li><code>id</code> property is designed to be autogenerated.
 *     A client has no possibility to update it manually.
 * <li><code>version</code> property is designed to be automatically updated
 *     by a persistence provider. A client has no possibility to update it manually.
 * <li><code>findAllConfigs</code> named query is designed to help
 *     in fetching a list of all configs sorted by the <code>modified</code> property.
 * <li>Deletion of an instance of the {@link ConstraintConfig} cascades to all the
 *     {@link ValidationRule}s and {@link OutputFile}s associated with it.
 * </ul>
 *
 * @author Aliaksandr Sazonenka
 * @see ValidationRule
 * @see OutputFile
 * @see OutputField
 */
@NamedQueries({
  @NamedQuery(name = "findAllConfigs",
      query = "SELECT c FROM ConstraintConfig c ORDER BY modified DESC"),
  @NamedQuery(name = "getConfigCount",
      query = "SELECT COUNT(c) FROM ConstraintConfig c")
})
@Entity
@Table(name = "CONSTRAINT_CONFIG")
public class ConstraintConfig extends GenericDomain {

  private static final long serialVersionUID = -535024201594361498L;

  @Column(name = "NAME")
  private String name;

  @Column(name = "AUTHOR")
  private String author;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "MODIFIED")
  private Date modified;

  /** Constructs a new {@link ConstraintConfig} instance. */
  public ConstraintConfig() {
    /* Default constructor to ensure serialization. */
  }

  /** Constructs a new {@link ConstraintConfig} instance with the given properties. */
  public ConstraintConfig(String name, String author, Date modified) {
    this.name = name;
    this.author = author;
    this.modified = new Date(modified.getTime());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ConstraintConfig that = (ConstraintConfig) obj;
    return Objects.equal(name, that.name)
        && Objects.equal(author, that.author);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, author);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("author", author)
            .add("modified", modified)
            .toString();
  }

  /** Returns the name of the dataset. */
  public String getName() { return name; }
  /** Sets the name of the dataset. */
  public void setName(String name) { this.name = name; }

  /** Returns the name of the dataset's author. */
  public String getAuthor() { return author; }
  /** Sets the name of the dataset's author. */
  public void setAuthor(String author) { this.author = author; }

  /** Returns the timestamp of the config's last modification. */
  public Date getModified() { return new Date(modified.getTime()); }
  /** Sets the timestamp of the config's last modification. */
  public void setModified(Date modified) { this.modified = new Date(modified.getTime()); }

}
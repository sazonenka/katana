package by.sazonenka.katana.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Generic interface that specifies a basic set of methods an entity must implement.
 *
 * @author Aliaksandr Sazonenka
 */
@MappedSuperclass
public abstract class GenericDomain implements Serializable {

  private static final long serialVersionUID = -2957451293924106999L;

  @Id
  @GeneratedValue
  @Column(name = "ID")
  protected Long id;

  @Version
  @Column(name = "VERSION")
  protected int version;

  /** Returns the identifier of the persistence instance. */
  public Long getId() { return id; }
  /** Sets the identifier of the persistence instance. */
  public void setId(Long id) { this.id = id; }

  /**
   * Returns the version of the persistence instance.
   *
   * <p>In the JPA architecture, the persistence provider uses
   * the <code>version</code> property to perform optimistic locking
   * and concurrency semantics for a persisting entity.
   */
  public Integer getVersion() { return version; }
  /** Sets the version of the persistence instance. */
  public void setVersion(Integer version) { this.version = version; }

}

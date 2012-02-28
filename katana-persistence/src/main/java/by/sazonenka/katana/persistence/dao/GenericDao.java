package by.sazonenka.katana.persistence.dao;

import java.io.Serializable;

/**
 * Generic interface that specifies a set of basic CRUD operations
 * which can be performed on persistent entities.
 *
 * @author Aliaksandr Sazonenka
 * @param <E> Entity type
 * @param <ID> Id type, for example Long
 */
public interface GenericDao<E, ID extends Serializable> {

  /**
   * Returns the persistent instance of the current entity type with the given identifier,
   * or <code>null</code> if not found.
   *
   * @param id the identifier of the persistent instance
   * @return the persistent instance
   */
  E get(ID id);

  /**
   * Saves or updates the given persistent instance.
   *
   * @param entity the persistent instance to save or update
   * @return the persisted instance
   */
  E save(E entity);

  /**
   * Deletes the given persistent instance.
   *
   * @param entity the persistent instance to delete
   */
  void delete(E entity);

  /**
   * Flushes all pending saves, updates and deletes to the database.
   * <p>
   * Only invoke this for selective eager flushing, for example when
   * JDBC code needs to see certain changes within the same transaction.
   * Else, it is preferable to rely on auto-flushing at transaction completion.
   */
  void flush();

}

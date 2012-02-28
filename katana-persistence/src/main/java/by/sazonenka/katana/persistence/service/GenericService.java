package by.sazonenka.katana.persistence.service;

import java.io.Serializable;

/**
 * Generic interface that specifies a set of business operations
 * which are exposed by this module.
 *
 * @author Aliaksandr Sazonenka
 * @param <E> Entity type
 * @param <ID> Id type, for example Long
 */
public interface GenericService<E, ID extends Serializable> {

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
   * @param id the identifier of the persistent instance
   */
  void delete(ID id);

}

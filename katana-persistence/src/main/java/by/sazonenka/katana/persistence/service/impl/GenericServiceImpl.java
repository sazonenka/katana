package by.sazonenka.katana.persistence.service.impl;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import by.sazonenka.katana.persistence.dao.GenericDao;
import by.sazonenka.katana.persistence.dao.impl.GenericDaoImpl;
import by.sazonenka.katana.persistence.service.GenericService;

import com.google.common.base.Preconditions;

/**
 * Generic base class providing implementation of business operations
 * which can be performed on persistent entities.
 * <p>
 * This class is a thin wrapper around {@link GenericDaoImpl}.
 * Consider the following mapping between <code>GenericServiceImpl</code>
 * and <code>GenericDaoImpl</code> classes:
 * <ul>
 * <li><code>GenericServiceImpl.get</code> - <code>GenericDaoImpl.get</code>
 * <li><code>GenericServiceImpl.save</code> - <code>GenericDaoImpl.save</code>
 * <li><code>GenericServiceImpl.delete</code> - <code>GenericDaoImpl.delete</code>
 * </ul>
 *
 * @author Aliaksandr Sazonenka
 * @param <E> Entity type
 * @param <ID> Id type, for example Long
 * @param <D> {@link GenericDao} implementation
 * @see GenericService
 */
@Transactional
public abstract class GenericServiceImpl<E, ID extends Serializable, D extends GenericDao<E, ID>>
    implements GenericService<E, ID> {

  protected final D dao;

  public GenericServiceImpl(D dao) {
    this.dao = Preconditions.checkNotNull(dao);
  }

  @Override
  public E get(ID id) {
    Preconditions.checkNotNull(id, "Got unexpected null 'id' passed to the method.");

    return dao.get(id);
  }

  @Override
  public E save(E entity) {
    Preconditions.checkNotNull(entity, "Got unexpected null 'entity' passed to the method.");

    return dao.save(entity);
  }

  @Override
  public void delete(ID id) {
    Preconditions.checkNotNull(id, "Got unexpected null 'id' passed to the method.");

    dao.delete(dao.get(id));
  }

}

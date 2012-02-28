package by.sazonenka.katana.persistence.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import by.sazonenka.katana.persistence.dao.GenericDao;

import com.google.common.base.Preconditions;

/**
 * Generic base class providing implementation of basic CRUD operations
 * which can be performed on persistent entities.
 * <p>
 * This class is a thin wrapper around {@link HibernateDaoSupport}.
 * Consider the following mapping between <code>GenericDaoImpl</code>
 * and <code>HibernateTemplate</code> classes:
 * <ul>
 * <li><code>GenericDaoImpl.get</code> - <code>HibernateTemplate.load</code>
 * <li><code>GenericDaoImpl.save</code> - <code>HibernateTemplate.saveOrUpdate</code>
 * <li><code>GenericDaoImpl.delete</code> - <code>HibernateTemplate.delete</code>
 * <li><code>GenericDaoImpl.flush</code> - <code>HibernateTemplate.flush</code>
 * <li><code>GenericDaoImpl.clear</code> - <code>HibernateTemplate.clear</code>
 * </ul>
 *
 * @author Aliaksandr Sazonenka
 * @param <E> Entity type
 * @param <ID> Id type, for example Long
 * @see HibernateDaoSupport
 * @see GenericDao
 */
public abstract class GenericDaoImpl<E, ID extends Serializable>
    extends HibernateDaoSupport
    implements GenericDao<E, ID> {

  private final Class<E> persistentClass;

  /**
   * Constructs a new {@link GenericDaoImpl} instance. Figures out
   * the actual class &lt;E&gt; of the persistent entity.
   */
  @SuppressWarnings("unchecked")
  public GenericDaoImpl() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    this.persistentClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
  }

  @Override
  public E get(ID id) {
    Preconditions.checkNotNull(id, "Got unexpected null 'id' passed to the method.");

    return getHibernateTemplate().get(persistentClass, id);
  }

  @Override
  public E save(E entity) {
    Preconditions.checkNotNull(entity, "Got unexpected null 'entity' passed to the method.");

    getHibernateTemplate().saveOrUpdate(entity);
    return entity;
  }

  @Override
  public void delete(E entity) {
    Preconditions.checkNotNull(entity, "Got unexpected null 'entity' passed to the method.");

    getHibernateTemplate().delete(entity);
  }

  @Override
  public void flush() {
    getHibernateTemplate().flush();
  }

  /** Sets the Hibernate SessionFactory to be used by this DAO. */
  @Inject
  public void setupSessionFactory(SessionFactory sessionFactory) {
    setSessionFactory(Preconditions.checkNotNull(sessionFactory));
  }

}

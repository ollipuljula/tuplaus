package fi.ollipuljula.tuplaus.db;

import fi.ollipuljula.tuplaus.db.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public abstract class AbstractDao<T extends AbstractEntity> {
    final private Class<T> clazz;

    @PersistenceContext
    private EntityManager em;

    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void create(T entity) {
        em.persist(entity);
    }

    public T find(long id) {
        return em.find(clazz, id);
    }

    public List<T> find() {
        return em.createQuery("from " + clazz.getName()).getResultList();
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public void delete(T entity) {
        em.remove(entity);
    }

    public void delete(long id) {
        delete(find(id));
    }
}

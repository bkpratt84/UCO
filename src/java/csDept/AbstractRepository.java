package csDept;

import java.util.List;
import javax.persistence.EntityManager;

public abstract class AbstractRepository<T> {

    private Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public T create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        
        return entity;
    }

    public T createOrUpdate(T entity, Integer id) {
        if (id == null) {
            getEntityManager().persist(entity);
            getEntityManager().flush();
            return entity;
        }

        T existingEntity = this.find(id);
        if (existingEntity == null) {
            getEntityManager().persist(entity);
        } else {
            getEntityManager().merge(entity);
        }
        
        getEntityManager().flush();
        
        return entity;
    }

    public void update(T entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}

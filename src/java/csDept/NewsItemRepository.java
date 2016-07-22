package csDept;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class NewsItemRepository extends AbstractRepository<Newsitem> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NewsItemRepository() {
        super(Newsitem.class);
    }

    public List<Newsitem> orderByDate() {
        Query query = em.createNamedQuery("Newsitem.orderByDate");
        List<Newsitem> itemList = query.getResultList();
        return itemList;
    }
}

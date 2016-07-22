package announcements.domain;

import csDept.AbstractRepository;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CategoryRepository extends AbstractRepository<Category> {
    
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public CategoryRepository() {
        super(Category.class);
    }

    /**
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Category GetByCategoryID(Integer catID) {
        Query query = em.createNamedQuery("Category.findByCategoryID");
        query.setParameter("categoryID", catID);
        Category cat = (Category) query.getSingleResult();
        
        return cat;
    }
    
    public List<Category> GetByInactive(boolean inactive) {
        Query query = em.createNamedQuery("Category.findAllByInactive");
        query.setParameter("inactive", inactive);
        List<Category> c = query.getResultList();
        
        return c;
    }
}
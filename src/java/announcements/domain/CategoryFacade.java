package announcements.domain;

import csDept.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CategoryFacade extends AbstractFacade<Category> {
    
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public CategoryFacade() {
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
}
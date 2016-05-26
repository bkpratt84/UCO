package csDept;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class DegreesFacade extends AbstractFacade<Degrees> {
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DegreesFacade() {
        super(Degrees.class);
    }
    
}

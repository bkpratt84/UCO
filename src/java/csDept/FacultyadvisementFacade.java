
package csDept;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FacultyadvisementFacade extends AbstractFacade<Facultyadvisement> {
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultyadvisementFacade() {
        super(Facultyadvisement.class);
    }
    
}

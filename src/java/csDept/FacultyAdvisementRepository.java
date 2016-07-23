
package csDept;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FacultyAdvisementRepository extends AbstractRepository<FacultyAdvisement> {
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultyAdvisementRepository() {
        super(FacultyAdvisement.class);
    }
    
}

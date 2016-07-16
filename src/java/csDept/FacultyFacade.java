package csDept;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FacultyFacade extends AbstractRepository<Faculty> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultyFacade() {
        super(Faculty.class);
    }

    public List<Faculty> findByStatus(String status) {
        Query query = em.createNamedQuery("Faculty.findByStatus");
        query.setParameter("status", status);
        List<Faculty> facultyList = query.getResultList();
        return facultyList;
    }

    public Integer findMaxId() {
        Query query = em.createNamedQuery("Faculty.findMaxID");
        Integer id = 0;
        List<Integer> facultyList = query.getResultList();
        id = facultyList.get(0);
        return id;
    }
}

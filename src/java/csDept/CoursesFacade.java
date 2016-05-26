
package csDept;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CoursesFacade extends AbstractFacade<Courses> {
    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoursesFacade() {
        super(Courses.class);
    }
    
}

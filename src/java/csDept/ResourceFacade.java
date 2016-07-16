
package csDept;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class ResourceFacade extends AbstractRepository<Resource> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResourceFacade() {
        super(Resource.class);
    }

    public Resource findByResourceId(int id) {
        Query query = em.createNamedQuery("Resource.findByResourceid");
        query.setParameter("resourceid", id);
        List<Resource> resourceList = query.getResultList();
        System.out.println("TITLE = " + resourceList.get(0).getTitle());
        return resourceList.get(0);
    }

}

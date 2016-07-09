package announcements.domain;

import csDept.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FileFacade extends AbstractFacade<File> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public FileFacade() {
        super(File.class);
    }

    /**
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<File> GetByPostId(Integer postID) {
        Query query = em.createNamedQuery("File.findByPostId");
        query.setParameter("postID", postID);
        List<File> files = query.getResultList();
        
        return files;
    }
}
package announcements.domain;

import csDept.AbstractRepository;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FileRepository extends AbstractRepository<File> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public FileRepository() {
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
        Query query = em.createNamedQuery("File.findByPostID");
        query.setParameter("postID", postID);
        List<File> files = query.getResultList();
        
        return files;
    }
    
    public File GetByFileID(Integer fileID) {
        Query query = em.createNamedQuery("File.findByFileID");
        query.setParameter("fileID", fileID);
        File file = (File) query.getSingleResult();
        
        return file;
    }
}
package announcements.domain;

import csDept.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PostsFacade extends AbstractFacade<Post> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public PostsFacade() {
        super(Post.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Post> GetByParentId(Integer parentId, boolean desc) {
        Query query;
        
        if (desc) {
            query = em.createNamedQuery("Post.findByParentIdDesc");
        } else {
            query = em.createNamedQuery("Post.findByParentIdAsc");
        }
        query.setParameter("parentId", parentId);
        List<Post> posts = query.getResultList();
        return posts;
    }
    
    public Post GetByPostId(Integer postId) {
        Query query = em.createNamedQuery("Post.findByPostId");
        query.setParameter("postId", postId);
        Post post = (Post) query.getSingleResult();
        return post;
    }
    
    public List<Post> Search(String searchText) {
        Query query = em.createNamedQuery("Post.search");
        query.setParameter("searchText", "%" + searchText + "%");
        List<Post> posts = query.getResultList();
        return posts;
    }
    
    public long GetFileCount(Integer postId) {
        Query query = em.createNamedQuery("Post.getFileCount");
        query.setParameter("postId", postId);
        
        return (long) query.getSingleResult();
    }
}
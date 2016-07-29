package announcements.domain;

import csDept.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PostRepository extends AbstractRepository<Post> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    public PostRepository() {
        super(Post.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Post> getByParentId(Integer parentId, boolean desc) {
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
    
    public Post getByPostId(Integer postId) {
        Query query = em.createNamedQuery("Post.findByPostId");
        query.setParameter("postId", postId);
        Post post = (Post) query.getSingleResult();
        return post;
    }
    
    public List<Post> search(String searchText) {
        Query query = em.createNamedQuery("Post.search");
        query.setParameter("searchText", "%" + searchText + "%");
        List<Post> posts = query.getResultList();
        return posts;
    }
    
    public long getFileCount(Integer postId) {
        Query query = em.createNamedQuery("Post.getFileCount");
        query.setParameter("postId", postId);
        
        return (long) query.getSingleResult();
    }
    
    public long getCommentCount(Integer parentId) {
        Query query = em.createNamedQuery("Post.getCommentCount");
        query.setParameter("parentId", parentId);
        
        return (long) query.getSingleResult();
    }
    
    public void softDelete(Post post) {
        post.setActive(false);
        this.update(post);
    }
}
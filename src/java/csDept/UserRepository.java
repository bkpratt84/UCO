package csDept;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserRepository extends AbstractRepository<User> {

    @PersistenceContext(unitName = "CSWebsitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRepository() {
        super(User.class);
    }

    public User GetByEmail(String email) {
        Query query = em.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User user = (User) query.getSingleResult();

        return user;
    }

    public List<User> GetByActive(boolean active) {
        Query query = em.createNamedQuery("User.findByActive");
        query.setParameter("active", active);
        List<User> users = query.getResultList();
        
        return users;
    }

    public List<User> GetAnnouncementSubscribers() {
        Query query = em.createNamedQuery("User.findAnnouncementSubscribers");
        List<User> users = query.getResultList();

        return users;
    }
}

package csDept;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        try {
            Query query = em.createNamedQuery("User.findByEmail");
            query.setParameter("email", email);
            User user = (User) query.getSingleResult();

            return user;
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public User GetByUserName(String userName) {
        try {
            Query query = em.createNamedQuery("User.findByUserName");
            query.setParameter("userName", userName);
            User user = (User) query.getSingleResult();

            return user;
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public User GetByID(int ID) {
        try {
            Query query = em.createNamedQuery("User.findByID");
            query.setParameter("ID", ID);
            User user = (User) query.getSingleResult();

            return user;
        } catch(NoResultException e) {
            return null;
        }
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
    
    public User GetByActivationKey(String key) {
        try {
            Query query = em.createNamedQuery("User.findByActivationCode");
            query.setParameter("key", key);
            User user = (User) query.getSingleResult();

            return user;
        } catch(NoResultException e) {
            return null;
        }
    }
}
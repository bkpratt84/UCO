package csDept;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import registration.User;

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

    public List<User> GetAnnouncementSubscribers() {
        Query query = em.createNamedQuery("User.findAnnouncementSubscribers");
        List<User> users = query.getResultList();

        return users;
    }
}

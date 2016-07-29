package csDept;

import announcements.domain.Post;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USER_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT user FROM User user"),
    @NamedQuery(name = "User.findByActive", query = "SELECT user FROM User user WHERE user.active = :active"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT user FROM User user WHERE user.username = :email"),
    @NamedQuery(name = "User.findByUserName", query = "SELECT user FROM User user WHERE user.username = :userName"),
    @NamedQuery(name = "User.findByID", query = "SELECT user FROM User user WHERE user.id = :ID"),
    @NamedQuery(name = "User.findAnnouncementSubscribers", query = "SELECT user FROM User user WHERE user.subscribedToAnnouncements = true AND user.active = true"),
    @NamedQuery(name = "User.findByActivationCode", query = "SELECT user FROM User user WHERE user.active = false AND user.activationKey = :key")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "firstName")
    private String firstName;
    
    @Column(name = "lastName")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String currentPassword;
    
    @Column(name = "active")
    private boolean active;

    @Column(name = "subscribedToAnnouncements")
    private boolean subscribedToAnnouncements;
        
    @Column(name = "activationKey")
    private String activationKey;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public boolean isSubscribedToAnnouncements() {
        return subscribedToAnnouncements;
    }

    public void setSubscribedToAnnouncements(boolean subscribedToAnnouncements) {
        this.subscribedToAnnouncements = subscribedToAnnouncements;
    }

    public User(int ID, String username, String firstName, String lastName, String email, Boolean active) {
        this.id = ID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
    }
}

package announcements.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "POST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findByParentId", query = "SELECT p FROM Post p WHERE p.parentId = :parentId")})
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "postId")
    private int postID;
    
    @Column(name = "parentId")
    private int parentId;
    
    @Column(name = "authorId")
    private int author;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "views")
    private int views;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dateCreated")
    private Date dateCreated;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dateModified")
    private Date dateModified;
    
    @Column(name = "modifiedBy")
    private int modifiedBy;
    
    @Column(name = "active")
    private boolean active;
    
//
//    public Post(int postID, int author, String title, String content, LocalDate dateCreated, LocalDate dateModified, int dateModifiedBy) {
//        this.postID = postID;
//        this.author = author;
//        this.title = title;
//        this.content = content;
//        this.dateModified = this.dateModified;
//        this.modifiedBy = modifiedBy;
//    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
    
    public int getParentID() {
        return parentId;
    }

    public void setParentID(int parentID) {
        this.parentId = parentID;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
    
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateModified = dateCreated;
    }
    
    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
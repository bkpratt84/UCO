package csDept;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "NEWSITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Newsitem.findAll", query = "SELECT n FROM Newsitem n"),
    @NamedQuery(name = "Newsitem.findById", query = "SELECT n FROM Newsitem n WHERE n.id = :id"),
    @NamedQuery(name = "Newsitem.findByTitle", query = "SELECT n FROM Newsitem n WHERE n.title = :title"),
    @NamedQuery(name = "Newsitem.orderByDate", query = "SELECT n FROM Newsitem n ORDER BY n.datepublished DESC"),

    @NamedQuery(name = "Newsitem.findByContent", query = "SELECT n FROM Newsitem n WHERE n.content = :content")})
public class Newsitem implements Serializable {

    @Basic(optional = false)
    @Column(name = "DATEPUBLISHED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datepublished;

    @PrePersist
    protected void onCreate() {
        datepublished = new Date();
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 1000)
    @Column(name = "CONTENT")
    private String content;

    public Newsitem() {
    }

    public Newsitem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Newsitem)) {
            return false;
        }
        Newsitem other = (Newsitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Newsitem[ id=" + id + " ]";
    }

    public Date getDatepublished() {
        return datepublished;
    }

    public void setDatepublished(Date datepublished) {
        this.datepublished = datepublished;
    }

}

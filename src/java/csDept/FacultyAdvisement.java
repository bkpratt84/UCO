
package csDept;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "FACULTYADVISEMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facultyadvisement.findAll", query = "SELECT f FROM FacultyAdvisement f"),
    @NamedQuery(name = "Facultyadvisement.findById", query = "SELECT f FROM FacultyAdvisement f WHERE f.id = :id"),
    @NamedQuery(name = "Facultyadvisement.findByTitle", query = "SELECT f FROM FacultyAdvisement f WHERE f.title = :title"),
    @NamedQuery(name = "Facultyadvisement.findByContent", query = "SELECT f FROM FacultyAdvisement f WHERE f.content = :content")})
public class FacultyAdvisement implements Serializable {
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

    public FacultyAdvisement() {
    }

    public FacultyAdvisement(Integer id) {
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
        if (!(object instanceof FacultyAdvisement)) {
            return false;
        }
        FacultyAdvisement other = (FacultyAdvisement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Facultyadvisement[ id=" + id + " ]";
    }
    
}

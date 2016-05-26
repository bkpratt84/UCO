
package csDept;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "RESOURCEITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resourceitem.findAll", query = "SELECT r FROM Resourceitem r"),
    @NamedQuery(name = "Resourceitem.findByItemid", query = "SELECT r FROM Resourceitem r WHERE r.itemid = :itemid"),
    @NamedQuery(name = "Resourceitem.findByTitle", query = "SELECT r FROM Resourceitem r WHERE r.title = :title"),
    @NamedQuery(name = "Resourceitem.findByContents", query = "SELECT r FROM Resourceitem r WHERE r.contents = :contents")})
public class Resourceitem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ITEMID")
    private Integer itemid;
    @Size(max = 64)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 255)
    @Column(name = "CONTENTS")
    private String contents;
    @JoinColumn(name = "RESOURCEID")
    @ManyToOne
    private Resource resourceid;

    public Resourceitem() {
    }

    public Resourceitem(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Resource getResourceid() {
        return resourceid;
    }

    public void setResourceid(Resource resourceid) {
        this.resourceid = resourceid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemid != null ? itemid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resourceitem)) {
            return false;
        }
        Resourceitem other = (Resourceitem) object;
        if ((this.itemid == null && other.itemid != null) || (this.itemid != null && !this.itemid.equals(other.itemid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Resourceitem[ itemid=" + itemid + " ]";
    }
    
}

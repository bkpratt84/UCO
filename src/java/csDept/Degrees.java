
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
@Table(name = "DEGREES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Degrees.findAll", query = "SELECT d FROM Degrees d"),
    @NamedQuery(name = "Degrees.findByDegreeId", query = "SELECT d FROM Degrees d WHERE d.degreeId = :degreeId"),
    @NamedQuery(name = "Degrees.findByDegreeName", query = "SELECT d FROM Degrees d WHERE d.degreeName = :degreeName"),
    @NamedQuery(name = "Degrees.findByDegreeDesc", query = "SELECT d FROM Degrees d WHERE d.degreeDesc = :degreeDesc"),
    @NamedQuery(name = "Degrees.findByDegreeCode", query = "SELECT d FROM Degrees d WHERE d.degreeCode = :degreeCode")})
public class Degrees implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEGREE_ID")
    private Integer degreeId;
    @Size(max = 255)
    @Column(name = "DEGREE_NAME")
    private String degreeName;
    @Size(max = 1000)
    @Column(name = "DEGREE_DESC")
    private String degreeDesc;
    @Size(max = 64)
    @Column(name = "DEGREE_CODE")
    private String degreeCode;

    public Degrees() {
    }

    public Degrees(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public Integer getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getDegreeDesc() {
        return degreeDesc;
    }

    public void setDegreeDesc(String degreeDesc) {
        this.degreeDesc = degreeDesc;
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (degreeId != null ? degreeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Degrees)) {
            return false;
        }
        Degrees other = (Degrees) object;
        if ((this.degreeId == null && other.degreeId != null) || (this.degreeId != null && !this.degreeId.equals(other.degreeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Degrees[ degreeId=" + degreeId + " ]";
    }
    
}

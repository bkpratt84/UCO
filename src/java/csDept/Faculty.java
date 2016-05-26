
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "FACULTY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Faculty.findAll", query = "SELECT f FROM Faculty f"),
    @NamedQuery(name = "Faculty.findByFacultyId", query = "SELECT f FROM Faculty f WHERE f.facultyId = :facultyId"),
    @NamedQuery(name = "Faculty.findByNameprefix", query = "SELECT f FROM Faculty f WHERE f.nameprefix = :nameprefix"),
    @NamedQuery(name = "Faculty.findByFirstname", query = "SELECT f FROM Faculty f WHERE f.firstname = :firstname"),
    @NamedQuery(name = "Faculty.findByLastname", query = "SELECT f FROM Faculty f WHERE f.lastname = :lastname"),
    @NamedQuery(name = "Faculty.findByJobtitle", query = "SELECT f FROM Faculty f WHERE f.jobtitle = :jobtitle"),
    @NamedQuery(name = "Faculty.findByEmail", query = "SELECT f FROM Faculty f WHERE f.email = :email"),
    @NamedQuery(name = "Faculty.findByWebsite", query = "SELECT f FROM Faculty f WHERE f.website = :website"),
    @NamedQuery(name = "Faculty.findByOffice", query = "SELECT f FROM Faculty f WHERE f.office = :office"),
    @NamedQuery(name = "Faculty.findByPhone", query = "SELECT f FROM Faculty f WHERE f.phone = :phone"),
    @NamedQuery(name = "Faculty.findByStatus", query = "SELECT f FROM Faculty f WHERE f.status = :status")})
public class Faculty implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FACULTY_ID")
    private Integer facultyId;
    @Size(max = 64)
    @Column(name = "NAMEPREFIX")
    private String nameprefix;
    @Size(max = 64)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 64)
    @Column(name = "LASTNAME")
    private String lastname;
    @Size(max = 64)
    @Column(name = "JOBTITLE")
    private String jobtitle;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 255)
    @Column(name = "WEBSITE")
    private String website;
    @Size(max = 64)
    @Column(name = "OFFICE")
    private String office;
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 64)
    @Column(name = "PHONE")
    private String phone;
    @Size(max = 64)
    @Column(name = "STATUS")
    private String status;

    public Faculty() {
    }

    public Faculty(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public String getNameprefix() {
        return nameprefix;
    }

    public void setNameprefix(String nameprefix) {
        this.nameprefix = nameprefix;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facultyId != null ? facultyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Faculty)) {
            return false;
        }
        Faculty other = (Faculty) object;
        if ((this.facultyId == null && other.facultyId != null) || (this.facultyId != null && !this.facultyId.equals(other.facultyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Faculty[ facultyId=" + facultyId + " ]";
    }
    
}

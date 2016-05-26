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
@Table(name = "COURSES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Courses.findAll", query = "SELECT c FROM Courses c"),
    @NamedQuery(name = "Courses.findByCourseId", query = "SELECT c FROM Courses c WHERE c.courseId = :courseId"),
    @NamedQuery(name = "Courses.findByCourseCrn", query = "SELECT c FROM Courses c WHERE c.courseCrn = :courseCrn"),
    @NamedQuery(name = "Courses.findByCourseName", query = "SELECT c FROM Courses c WHERE c.courseName = :courseName"),
    @NamedQuery(name = "Courses.findByCourseDesc", query = "SELECT c FROM Courses c WHERE c.courseDesc = :courseDesc"),
    @NamedQuery(name = "Courses.findByCoursePrereq", query = "SELECT c FROM Courses c WHERE c.coursePrereq = :coursePrereq")})
public class Courses implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COURSE_ID")
    private Integer courseId;
    @Size(max = 255)
    @Column(name = "COURSE_CRN")
    private String courseCrn;
    @Size(max = 255)
    @Column(name = "COURSE_NAME")
    private String courseName;
    @Size(max = 1000)
    @Column(name = "COURSE_DESC")
    private String courseDesc;
    @Size(max = 1000)
    @Column(name = "COURSE_PREREQ")
    private String coursePrereq;

    public Courses() {
    }

    public Courses(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseCrn() {
        return courseCrn;
    }

    public void setCourseCrn(String courseCrn) {
        this.courseCrn = courseCrn;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCoursePrereq() {
        return coursePrereq;
    }

    public void setCoursePrereq(String coursePrereq) {
        this.coursePrereq = coursePrereq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseId != null ? courseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Courses)) {
            return false;
        }
        Courses other = (Courses) object;
        if ((this.courseId == null && other.courseId != null) || (this.courseId != null && !this.courseId.equals(other.courseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csDept.Courses[ courseId=" + courseId + " ]";
    }
    
}

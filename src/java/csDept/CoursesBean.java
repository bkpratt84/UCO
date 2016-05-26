package csDept;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "courseBean")
@SessionScoped
public class CoursesBean implements Serializable {

    // the same properties from Course Entity class
    private Integer courseId;
    private String courseCrn;
    private String courseName;
    private String courseDesc;
    private String coursePrereq;

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

}

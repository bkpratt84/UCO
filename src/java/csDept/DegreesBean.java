package csDept;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "degreeBean")
@SessionScoped
public class DegreesBean implements Serializable {

    // the same properties from Degree Entity class
    private Integer degreeId;
    private String degreeName;
    private String degreeDesc;
    private String degreeCode;

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
    
}

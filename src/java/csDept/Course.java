package csDept;

public class Course {
    
    public Course(int id, String crn, String name, String desc, String prereq){
        this.id = id;
        this.crn = crn;
        this.name = name;
        this.desc = desc;
        this.prereq = prereq;
    }
    public Course(){
        //empty constructor
    };
    
    private int id;
    private String crn;
    private String name;
    private String desc;
    private String prereq;


    public String getPrereq() {
        return prereq;
    }

    public void setPrereq(String prereq) {
        this.prereq = prereq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}

package bookPackage;

public class BookClass {
    private String crn;
    private String subject;
    private String course;
    private String title;
    private String days;
    private String times;
    private int capacity;
    private String instructor;
    
    public BookClass(String crn, String subject, String course, String title, String days, String times, int capacity, String instructor){
        this.crn=crn;
        this.subject=subject;
        this.course=course;
        this.title=title;
        this.days=days;
        this.times=times;
        this.capacity=capacity;
        this.instructor=instructor;
    }
    
    public void setCrn(String crn){
        this.crn = crn;
    }
    public String getCrn(){
        return crn;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return subject;
    }
    public void setCourse(String course){
        this.course = course;
    }
    public String getCourse(){
        return course;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setDays(String days){
        this.days = days;
    }
    public String getDays(){
        return days;
    }
    public void setTimes(String times){
        this.times = times;
    }
    public String getTimes(){
        return times;
    }
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
    public int getCapacity(){
        return capacity;
    }
    public void setInstructor(String instructor){
        this.instructor = instructor;
    }
    public String getInstructor(){
        return instructor;
    }
}

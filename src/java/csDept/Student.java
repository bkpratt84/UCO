package csDept;

public class Student {
private String username;
private String firstname;
private String lastname;
private String active;
private int id;

    public Student(String username, String firstname, String lastname, String active, int id) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.active = active;
        this.id = id;
    }

    public Student() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}

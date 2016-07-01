package announcements.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Registration implements Serializable {
    @Size(min = 3, max = 30, message = "Please enter a name between 2 and 30 characters.")
    private String firstName;
    
    @Size(min = 3, max = 30, message = "Please enter a name between 2 and 30 characters.")
    private String lastName;

    @Pattern(regexp = "^[a-z][\\w.+-]+@uco\\.edu$", message = "Format: username@uco.edu")
    private String email;
    
    @Size(min = 8, max = 30, message = "Please enter a password between 8 and 30 characters.")
    private String pw;
    
    @Size(min = 8, max = 30, message = "Please enter a password between 8 and 30 characters.")
    private String pwConfirm;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPwConfirm() {
        return pwConfirm;
    }

    public void setPwConfirm(String pwConfirm) {
        this.pwConfirm = pwConfirm;
    }   
}
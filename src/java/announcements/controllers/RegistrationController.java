package announcements.controllers;

import announcements.domain.Registration;
import announcements.services.UserService;
import announcements.utility.Messages;
import announcements.utility.Utility;
import csDept.User;
import csDept.UserRepository;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import registration.GoogleMail;

@Named(value = "registrationController")
@ViewScoped
public class RegistrationController implements Serializable {

    private Registration registrationUser;

    @EJB
    UserRepository userRepository;
    
    @Inject
    UserService userService;
    
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
    
    private boolean complete;

    @PostConstruct
    public void Init() {
        registrationUser = new Registration();
    }

    public Registration getRegistrationUser() {
        return registrationUser;
    }

    public void setRegistrationUser(Registration reg) {
        this.registrationUser = reg;
    }
    
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void submit() throws SQLException, MessagingException {
        if (userService.emailExists(this.email)) {
            Messages.setErrorMessage("An account with this email address already exists.", "errorMsg");
            return;
        }
        
        User user = new User();
        
        String activationCode = UUID.randomUUID().toString().substring(0, 6);

        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setUsername(this.email);
        user.setCurrentPassword(Utility.encryptPassword(this.pw));
        user.setActivationKey(activationCode);
        user.setActive(false);
        
        this.userRepository.create(user);
        this.complete = true;
        
        sendRegistrationEmail(activationCode);
    }

    private void sendRegistrationEmail(String activationCode) throws MessagingException {
        String message = String.format(GoogleMail.RegistrationMessage, this.firstName, this.lastName, activationCode);

        GoogleMail.Send(
                "UCOComputerScience",
                "sungisthebest",
                this.email,
                "Please activate your UCO CS Department login.",
                message);
    }
}
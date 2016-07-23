package csDept;

import announcements.domain.Registration;
import announcements.services.UserService;
import announcements.utility.Messages;
import announcements.utility.Utility;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Named(value = "editStudentController")
@ViewScoped
public class EditStudentController implements Serializable {

    private Registration registrationUser;

    @EJB
    UserRepository userRepository;

    @EJB
    UserGroupRepository userGroupRepository;

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

    private boolean subscribeToAnnouncements;

    private User currentUser;

    private boolean complete;

    @PostConstruct
    public void Init() {
        currentUser = userService.getCurrentUser();

        this.setFirstName(currentUser.getFirstName());
        this.setLastName(currentUser.getLastName());
        this.setEmail(currentUser.getEmail());
        this.setSubscribeToAnnouncements(currentUser.isSubscribedToAnnouncements());

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

    public boolean isSubscribeToAnnouncements() {
        return subscribeToAnnouncements;
    }

    public void setSubscribeToAnnouncements(boolean subscribeToAnnouncements) {
        this.subscribeToAnnouncements = subscribeToAnnouncements;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void submit() throws SQLException, MessagingException, IOException {
        if (userService.emailExists(this.email) && !this.email.equals(this.currentUser.getEmail())) {

            Messages.setErrorMessage("An account with this email address already exists.", "errorMsg");
            return;
        }

        currentUser.setFirstName(this.firstName);
        currentUser.setLastName(this.lastName);
        currentUser.setEmail(this.email);
//        user.setCurrentPassword(Utility.encryptPassword(this.pw));
        currentUser.setSubscribedToAnnouncements(this.subscribeToAnnouncements);

        this.userRepository.update(currentUser);
        this.complete = true;

        Messages.setSuccessMessage("Profile updated.");
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces/announcements.xhtml");
    }
}

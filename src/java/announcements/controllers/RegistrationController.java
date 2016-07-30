package announcements.controllers;

import announcements.domain.Registration;
import announcements.utility.EmailTemplate;
import announcements.utility.Messages;
import announcements.utility.Utility;
import csDept.User;
import csDept.UserGroup;
import csDept.UserRepository;
import csDept.UserGroupRepository;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang.WordUtils;
import org.omnifaces.util.Faces;
import registration.GoogleMail;

@Named(value = "registrationController")
@ViewScoped
public class RegistrationController implements Serializable {

    private Registration registrationUser;

    @EJB
    UserRepository userRepository;

    @EJB
    UserGroupRepository userGroupRepository;

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

    private boolean subscribeToAnnouncements = true;
    
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

    public boolean isSubscribeToAnnouncements() {
        return subscribeToAnnouncements;
    }

    public void setSubscribeToAnnouncements(boolean subscribeToAnnouncements) {
        this.subscribeToAnnouncements = subscribeToAnnouncements;
    }

    public void submit() throws IOException, MessagingException {
        if (this.userRepository.GetByUserName(this.email) != null) {
            Messages.setErrorMessage("An account with this email address already exists.", "errorMsg");
            return;
        }

        User user = new User();

        String activationCode = UUID.randomUUID().toString().substring(0, 6);
        
        this.firstName = WordUtils.capitalize(this.firstName.toLowerCase());
        this.lastName = WordUtils.capitalize(this.lastName.toLowerCase());
        
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setUsername(this.email);
        user.setCurrentPassword(Utility.encryptPassword(this.pw));
        user.setSubscribedToAnnouncements(this.subscribeToAnnouncements);
        user.setActivationKey(activationCode);
        user.setActive(false);

        UserGroup ug = new UserGroup();

        ug.setGroupname("student");
        ug.setUsername(this.email);

        this.userRepository.create(user);
        this.userGroupRepository.create(ug);
        this.complete = true;

        sendRegistrationEmail(activationCode);
    }

    private void sendRegistrationEmail(String activationCode) throws MessagingException, FileNotFoundException, IOException {
        HttpServletRequest request = (HttpServletRequest) Faces.getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = context.getRealPath("/resources/templates");

        Map<String, String> map = new HashMap<>();
        map.put("logoURL", "http://res.cloudinary.com/csuco/image/upload/v1469676951/uco_logo_e9qfdt.png");
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("activationCode", activationCode);
        map.put("buttonURL", baseURL + "faces/accountVerification.xhtml");
        map.put("fbURL", "http://res.cloudinary.com/csuco/image/upload/v1469676951/facebook_w1xzcx.png");
        
        String message = EmailTemplate.getEmailHTML(path, "studentRegister.vm", map);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        new Thread(() -> {
            try {
                GoogleMail.Send(
                        externalContext,
                        this.email,
                        "Please activate your UCO CS Department login.",
                        message);
            } catch (MessagingException ex) {
                Logger.getLogger(ThreadAddController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
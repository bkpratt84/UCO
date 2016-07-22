package announcements.controllers;

import announcements.domain.Registration;
import announcements.services.UserService;
import announcements.utility.Messages;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import csDept.UserRepository;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import registration.GoogleMail;
import registration.User;

@Named(value = "registrationController")
@ViewScoped
public class RegistrationController implements Serializable {

    private Registration registrationUser;

    @EJB
    UserRepository userRepository;
    
    @Inject
    UserService userService;

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
//
//    public String getActivationCode() {
//        return activationCode;
//    }
//
//    public void setActivationCode(String activationCode) {
//        this.activationCode = activationCode;
//    }

    public void submit() throws SQLException, MessagingException {
        if (userService.emailExists(registrationUser.getEmail())) {
            Messages.setErrorMessage("An account with this email address already exists.", "errorMsg");
            return;
        }

        String activationCode = UUID.randomUUID().toString().substring(0, 6);

        
        
        
        sendRegistrationEmail(activationCode);
    }

//    public void activateUser() {
//        String decodedUserEmail = new String(Base64.decode(activationCode), StandardCharsets.UTF_8);
//        User user = userRepository.GetByEmail(decodedUserEmail);
//        
//    }

    private void sendRegistrationEmail(String activationCode) throws MessagingException {
        String message = String.format(GoogleMail.RegistrationMessage, registrationUser.getFirstName(), registrationUser.getLastName(), activationCode);

        GoogleMail.Send(
                "UCOComputerScience",
                "sungisthebest",
                registrationUser.getEmail(),
                "Please activate your UCO CS Department login.",
                message);
    }
}

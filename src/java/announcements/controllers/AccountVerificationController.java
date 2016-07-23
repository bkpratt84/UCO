package announcements.controllers;

import announcements.utility.Messages;
import csDept.User;
import csDept.UserRepository;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.Size;

@Named(value = "accountVerificationController")
@ViewScoped
public class AccountVerificationController implements Serializable {
    @EJB
    UserRepository userRepository;
    
    @Size(min = 6, max = 6, message = "Enter your 6 digit code.")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public void submit() {
        User user = userRepository.GetByActivationKey(code);
        
        if (user == null) {
            Messages.setErrorMessage("Invalid code.");
        } else {
            user.setActive(true);
            userRepository.createOrUpdate(user, user.getId());
            Messages.setSuccessMessage("Success");
        }
    }
}
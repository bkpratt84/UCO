package announcements.controllers;

import announcements.domain.Registration;
import announcements.services.UserService;
import announcements.utility.Messages;
import csDept.Resourceitem;
import java.io.Serializable;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "registrationController")
@ViewScoped
public class RegistrationController implements Serializable {
    private Registration reg;
    
    @Inject
    UserService userService;
    
    @PostConstruct
    public void Init(){
       reg = new Registration(); 
    }
    
    public Registration getReg() {
        return reg;
    }

    public void setReg(Registration reg) {
        this.reg = reg;
    }
    
    public void submit() throws SQLException {
        if (userService.emailExists(reg.getEmail())) {
            Messages.setErrorMessage("An account with this email address already exists.", "errorMsg");
        } else {
            System.out.println("Available");
        }
    }
}
package announcements.controllers;

import announcements.domain.PostRepository;
import csDept.User;
import csDept.UserRepository;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Faces;

@Named(value = "utilityController")
@RequestScoped
public class UtilityController {

    @EJB
    UserRepository userRepository;
    
    @EJB
    PostRepository postRepository;
    
    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath());
    }
    
    public int getLoggedInID() {
        User user = this.userRepository.GetByUserName(Faces.getExternalContext().getUserPrincipal().getName());
        
        return user != null ? user.getId() : 0;
    }
    
    public long getCommentCount(int authorID) {
        return this.postRepository.getCommentCount(authorID);
    }
}
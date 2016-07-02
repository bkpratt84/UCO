package announcements.controllers;

import announcements.domain.Post;
import announcements.domain.PostsFacade;
import announcements.services.UserService;
import announcements.utility.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Named(value = "threadAddController")
@ViewScoped
public class ThreadAddController implements Serializable {

    @EJB
    PostsFacade postFacade;

    @Inject
    UserService userService;

//    Post post;
    Integer postId;
    String title;
    String content;
    String category;

    @PostConstruct
    public void ThreadAddController() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = request.getParameter("postId");
        if (id == null) {
            return;
        }

        postId = Integer.parseInt(id);

        Post post = postFacade.GetByPostId(postId);
        setTitle(post.getTitle());
        setContent(post.getContent());
        setCategory(post.getCategory());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void save() throws SQLException, IOException {
        Post post = new Post();
        
        Calendar cal = Calendar.getInstance();
        String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        int userId = userService.getUserId(userName);    
        
        if (postId != null) {
            post = postFacade.GetByPostId(postId);
            post.setDateModified(cal.getTime());
            post.setModifiedBy(userId);
        } else {
            post.setDateCreated(cal.getTime());
            post.setAuthor(userId);
        }

        post.setTitle(this.getTitle());
        post.setContent(this.getContent());
        post.setCategory(this.getCategory());
        post.setActive(true);

        postFacade.createOrUpdate(post, postId);

        Messages.setSuccessMessage("Announcement created.");
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces/announcements.xhtml");
    }
}

package announcements.controllers;

import announcements.domain.Post;
import announcements.domain.PostsFacade;
import announcements.services.UserService;
import announcements.utility.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

@Named(value = "postCommentsController")
@ViewScoped
public class postCommentsController implements Serializable {
    
    @EJB
    PostsFacade postFacade;
    
    @Inject
    UserService userService;
    
    private int postID;
    private List<Post> posts;
    private Post post;
    private String msg;

    public void init() {
        posts = postFacade.GetByParentId(postID);
        post = postFacade.GetByPostId(postID);
        
        if (posts == null) {
            posts = new ArrayList<>();
        }
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Post getPost() {
        return post;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public void save() throws SQLException, IOException {
        Calendar cal = Calendar.getInstance();

        String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();

        Post comment = new Post();


        comment.setContent(this.getMsg());
        comment.setDateCreated(cal.getTime());
        comment.setActive(true);
        comment.setAuthor(userService.getUserId(userName));
        comment.setParentID(postID);

        postFacade.create(comment);

        Messages.setSuccessMessage("Comment added.");
        
        init();
        RequestContext.getCurrentInstance().execute("PF('editorWidget').clear();");
    }
}
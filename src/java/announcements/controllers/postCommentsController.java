package announcements.controllers;

import announcements.domain.Post;
import announcements.domain.PostsFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "postCommentsController")
@ViewScoped
public class postCommentsController implements Serializable {
    
    @EJB
    PostsFacade postFacade;
    
    private int postID;
    private List<Post> posts;
    private Post post;

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
}
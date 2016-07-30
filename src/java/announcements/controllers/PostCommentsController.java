package announcements.controllers;

import announcements.domain.File;
import announcements.domain.FileRepository;
import announcements.domain.Post;
import announcements.domain.PostRepository;
import announcements.utility.Messages;
import csDept.User;
import csDept.UserRepository;
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
import javax.validation.constraints.Size;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;

@Named(value = "postCommentsController")
@ViewScoped
public class PostCommentsController implements Serializable {
    
    @EJB
    PostRepository postRepo;
    
    @EJB
    FileRepository fileRepo;
    
    @EJB
    UserRepository userRepository;
    
    private int postID;
    private int editPostID;
    private List<Post> posts;
    private Post post;
    
    @Size(min = 1, message = "Please enter a message.")
    private String msg;
    
    @Size(min = 1, message = "Please enter a message.")
    private String editMsg;
    
    List<File> filesAttached;

    public void init() {
        msg = null;
        editMsg = null;
        editPostID = 0;
        
        posts = postRepo.getByParentId(postID, false);
        post = postRepo.getByPostId(postID);
        
        if (filesAttached == null) {
            filesAttached = new ArrayList<>();
        }
        
        filesAttached = fileRepo.GetByPostId(postID);
        
        if (posts == null) {
            posts = new ArrayList<>();
        }
        
        post.incrementViews();
        postRepo.update(post);
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getEditPostID() {
        return editPostID;
    }

    public void setEditPostID(int editPostID) {
        this.editPostID = editPostID;
    }

    public String getEditMsg() {
        return editMsg;
    }

    public void setEditMsg(String editMsg) {
        this.editMsg = editMsg;
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

    public List<File> getFilesAttached() {
        return filesAttached;
    }
    
    public void edit(Post comment) {
        this.editPostID = comment.getPostID();
        this.editMsg = comment.getContent();
    }
    
    public void save() {
        Calendar cal = Calendar.getInstance();

        String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        User user = this.userRepository.GetByUserName(userName);
        
        Post comment = new Post();
        
        if (editPostID == 0) {
            comment.setContent(this.getMsg());
            comment.setDateCreated(cal.getTime());
            comment.setActive(true);
            comment.setAuthor(user.getId());
            comment.setParentID(postID);

            comment.setUser(user);
            postRepo.create(comment);
            
            Messages.setSuccessMessage("Comment added.");
        } else {
            comment.setContent(this.editMsg);
            comment.setModifiedBy(user.getId());
            comment.setDateModified(cal.getTime());
            
            postRepo.createOrUpdate(post, this.editPostID);
            
            Messages.setSuccessMessage("Changes saved!.");
        }
        
        init();
        RequestContext.getCurrentInstance().execute("PF('editorWidget').clear();");
    }
    
    public void saveComment(Post comment) {
        Calendar cal = Calendar.getInstance();

        String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        User user = this.userRepository.GetByUserName(userName);
        
        comment.setContent(this.editMsg);
        comment.setModifiedBy(user.getId());
        comment.setDateModified(cal.getTime());

        postRepo.createOrUpdate(comment, this.editPostID);

        Messages.setSuccessMessage("Changes saved!.");
        
        init();
        RequestContext.getCurrentInstance().execute("PF('editPostWidget').clear();");
    }

    public void delete(int postId, boolean parent) throws IOException {
        Post item = postRepo.getByPostId(postId);
        
        if (parent) {
            List<Post> items = postRepo.getByParentId(postId, false);
            List<File> files = fileRepo.GetByPostId(postId);
            
            for (Post p : items) {
                postRepo.softDelete(p);
            }
            
            for (File f : files) {
                fileRepo.remove(f);
            }
        }
        
        postRepo.softDelete(item);
        
        Messages.setSuccessMessage("Comment deleted.");
        
        if (parent) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getFlash().setKeepMessages(true);
            context.redirect(context.getRequestContextPath() + "/faces/announcements.xhtml");
        } else {
            init();
        }
    }
    
    public void download(File file) throws IOException {
        Faces.sendFile(file.getFileContent(), file.getFileName(), true);
    }
}
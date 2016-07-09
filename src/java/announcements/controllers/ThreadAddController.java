package announcements.controllers;

import announcements.domain.File;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "threadAddController")
@ViewScoped
public class ThreadAddController implements Serializable {

    @EJB
    PostsFacade postFacade;

    @Inject
    UserService userService;

    Integer postId;
    
    @Size(min = 5, max = 255, message = "Enter a title between 5 and 255 characters.")
    String title;
    
    @Size(min = 1, message = "Please enter a message.")
    String content;
    String category;
    
    List<File> filesAttached;
    List<File> filesPending;

    @PostConstruct
    public void ThreadAddController() {
        if (filesPending == null) {
            filesPending = new ArrayList<>();
        }
        
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

    public List<File> getFilesAttached() {
        return filesAttached;
    }

    public List<File> getFilesPending() {
        return filesPending;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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
        
        System.out.println(String.valueOf(this.getContent().length()));
        postFacade.createOrUpdate(post, postId);
        //Post p = postFacade.createOrUpdate(post, postId);
        
        //System.out.println(String.valueOf(p.getPostID()));
        
        Messages.setSuccessMessage("Announcement created.");
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + "/faces/announcements.xhtml");
    }
    
    public void fileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        long fileSize = uploadedFile.getSize();
        String fileType = uploadedFile.getContentType();
        byte[] contents = uploadedFile.getContents();
        
        File file = new File(fileName, fileType, fileSize, contents);
        
        filesPending.add(file);
    }
    
    public void removeFile(File file) {
        filesPending.remove(file);
    }
}
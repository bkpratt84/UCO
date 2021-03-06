package announcements.controllers;

import announcements.domain.Category;
import announcements.domain.CategoryRepository;
import announcements.domain.File;
import announcements.domain.FileRepository;
import announcements.domain.Post;
import announcements.domain.PostRepository;
import announcements.utility.EmailTemplate;
import announcements.utility.Messages;
import announcements.utility.Utility;
import csDept.UserRepository;
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
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import registration.GoogleMail;
import csDept.User;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.omnifaces.util.Faces;

@Named(value = "threadAddController")
@ViewScoped
public class ThreadAddController implements Serializable {

    @EJB
    private PostRepository postRepo;

    @EJB
    private FileRepository fileRepo;

    @EJB
    private UserRepository userRepo;

    @EJB
    private CategoryRepository categoryRepo;

    private Integer postId;

    @Size(min = 5, max = 255, message = "Enter a title between 5 and 255 characters.")
    private String title;

    @Size(min = 1, message = "Please enter a message.")
    private String content;
    private Category category;

    private List<File> filesAttached;
    private List<File> filesToDelete;
    private List<File> filesPending;
    private List<Category> categories;

    @PostConstruct
    public void ThreadAddController() {
        if (filesPending == null) {
            filesPending = new ArrayList<>();
        }

        if (filesAttached == null) {
            filesAttached = new ArrayList<>();
        }

        if (filesToDelete == null) {
            filesToDelete = new ArrayList<>();
        }

        if (categories == null) {
            categories = categoryRepo.GetByInactive(false);
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = request.getParameter("postId");
        if (id == null) {
            return;
        }

        postId = Integer.parseInt(id);

        Post post = postRepo.getByPostId(postId);
        Category cat = categoryRepo.GetByCategoryID(post.getCategoryId());

        setTitle(post.getTitle());
        setContent(post.getContent());
        setCategory(cat);

        filesAttached = fileRepo.GetByPostId(postId);
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<File> getFilesAttached() {
        return filesAttached;
    }

    public List<File> getFilesPending() {
        return filesPending;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void save() throws SQLException, IOException, MessagingException {
        Post post = new Post();

        Calendar cal = Calendar.getInstance();
        String userName = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        
        User authorUser = this.userRepo.GetByUserName(userName);
        
        if (postId != null) {
            post = postRepo.getByPostId(postId);
            post.setDateModified(cal.getTime());
            post.setModifiedBy(authorUser.getId());

            for (File f : filesToDelete) {
                fileRepo.remove(f);
            }
        } else {
            post.setDateCreated(cal.getTime());
            post.setAuthor(authorUser.getId());
            post.setUser(authorUser);
        }

        post.setTitle(this.getTitle());
        post.setContent(this.getContent());
        post.setCategoryId(this.category.getCategoryID());
        post.setCategory(category);
        post.setActive(true);

        Post p = postRepo.createOrUpdate(post, postId);

        persistFiles(filesPending, p.getPostID(), authorUser.getId());

        p.setFileCount(postRepo.getFileCount(p.getPostID()));
        p = postRepo.createOrUpdate(p, p.getPostID());

        if (this.userRepo.GetAnnouncementSubscribers().size() > 0 && postId == null) {
            for (User subscriber : this.userRepo.GetAnnouncementSubscribers()) {
                if (authorUser.getId() != subscriber.getId()) {
                    HttpServletRequest request = (HttpServletRequest) Faces.getExternalContext().getRequest();
                    String url = request.getRequestURL().toString();
                    String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

                    ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String path = context.getRealPath("/resources/templates");

                    Map<String, String> map = new HashMap<>();
                    map.put("logoURL", "http://res.cloudinary.com/csuco/image/upload/v1469676951/uco_logo_e9qfdt.png");
                    map.put("firstName", subscriber.getFirstName());
                    map.put("lastName", subscriber.getLastName());
                    map.put("authorFirstName", p.getUser().getFirstName());
                    map.put("authorLastName", p.getUser().getLastName());
                    map.put("title", post.getTitle());
                    map.put("category", post.getCategory().getCategory());
                    map.put("buttonURL", baseURL + "faces/thread.xhtml?post=" + String.valueOf(post.getPostID()));
                    map.put("fbURL", "http://res.cloudinary.com/csuco/image/upload/v1469676951/facebook_w1xzcx.png");

                    String message = EmailTemplate.getEmailHTML(path, "newAnnouncement.vm", map);
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    
                    new Thread(() -> {
                        try {
                            GoogleMail.Send(externalContext,
                            subscriber.getEmail(),
                            "New CS Announcement",
                            message);
                        } catch (MessagingException ex) {
                            Logger.getLogger(ThreadAddController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }).start();
                }
            }
        }
        
        String growl;
        
        if (postId == null) {
            growl = "Announcement created.";
        } else {
            growl = "Announcement saved!";
        }

        Messages.setSuccessMessage(growl);
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getFlash().setKeepMessages(true);
        context.redirect(context.getRequestContextPath() + "/faces/announcements.xhtml");
    }

    private void persistFiles(List<File> files, Integer postID, int ownerID) {
        for (File f : files) {
            f.setPostID(postID);
            f.setOwnerID(ownerID);
            fileRepo.createOrUpdate(f, postID);
        }
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
        if (postId != null) {
            filesToDelete.add(file);
            filesAttached.remove(file);
        } else {
            filesPending.remove(file);
        }
    }
}
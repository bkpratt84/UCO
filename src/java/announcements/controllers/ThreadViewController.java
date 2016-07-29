package announcements.controllers;

import announcements.domain.File;
import announcements.domain.FileRepository;
import announcements.domain.Post;
import announcements.domain.PostRepository;
import announcements.utility.Messages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

@Named(value = "threadViewController")
@ViewScoped
public class ThreadViewController implements Serializable {

    @EJB
    private PostRepository postRepo;
    
    @EJB
    private FileRepository fileRepo;

    private List<Post> threads;

    private String searchText;
    
    @PostConstruct
    public void init() {
        refresh();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void refresh() {
        threads = postRepo.getByParentId(0, true);

        if (threads == null) {
            threads = new ArrayList<>();
        }
    }

    public List<Post> getThreads() {
        return threads;
    }

    public void search(String searchText) {
        threads = postRepo.search(searchText);
    }
    
    public void delete(int postId) {
        Post post = postRepo.getByPostId(postId);
        List<Post> posts = postRepo.getByParentId(postId, true);
        List<File> files = fileRepo.GetByPostId(postId);

        for (Post p : posts) {
            postRepo.softDelete(p);
        }
        
        for (File f : files) {
            fileRepo.remove(f);
        }

        postRepo.softDelete(post);

        Messages.setSuccessMessage("Announcement deleted.");

        refresh();
        RequestContext.getCurrentInstance().update("frmAdd");
    }
}
package announcements.controllers;

import announcements.domain.Post;
import announcements.domain.PostRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

@Named(value = "threadViewController")
@ViewScoped
public class ThreadViewController implements Serializable {

    @EJB
    private PostRepository postFacade;

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
        threads = postFacade.getByParentId(0, true);

        if (threads == null) {
            threads = new ArrayList<>();
        }
    }

    public List<Post> getThreads() {
        return threads;
    }

    public void search(String searchText) {
        threads = postFacade.search(searchText);
    }
    
    public void delete(int postId) {
        Post post = postFacade.getByPostId(postId);
        List<Post> posts = postFacade.getByParentId(postId, true);

        for (Post p : posts) {
            postFacade.softDelete(p);
        }

        postFacade.softDelete(post);

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Success", "Announcement deleted."));

        refresh();
        RequestContext.getCurrentInstance().update("frmAdd");
    }
}
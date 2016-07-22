package csDept;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;

@Named(value = "newsitemController")
@SessionScoped
public class NewsitemController implements Serializable {

    @EJB
    NewsItemRepository newsitemFacade;

    @Inject
    NewsitemBean newsitemBean;

    List<Newsitem> newsitems;

    Newsitem newsitemTemp;
    List<Newsitem> reverse;

    @PostConstruct
    public void init() {
        load();
        newsitemTemp = new Newsitem();

    }

    public void load() {
        this.newsitems = newsitemFacade.orderByDate();
        this.reverse = newsitemFacade.orderByDate();
    }

    public List<Newsitem> getNewsitem() {
        return newsitems;
    }

    public Newsitem getNewsitemTemp() {
        return newsitemTemp;
    }

    public void setNewsitemTemp(Newsitem newsitemTemp) {
        this.newsitemTemp = newsitemTemp;
    }

    public Newsitem findById(int id) {
        return newsitemFacade.find(id);
    }

    public void setNewsitem(List<Newsitem> newsitems) {
        this.newsitems = newsitems;
    }

    public List<Newsitem> getAll() {
        return newsitemFacade.findAll();
    }

    public List<Newsitem> getAllReverse() {
        return reverse;
    }

    public int count() {
        return newsitemFacade.count();
    }

    public void delete(Newsitem c) {
        newsitemFacade.remove(c);
        load();
        //return null;
    }

    public String add() {

        Newsitem c = new Newsitem();
        c.setTitle(newsitemBean.getTitle());
        c.setContent(newsitemBean.getContent());
        newsitemFacade.create(c);
        newsitemBean.setContent(null);
        newsitemBean.setTitle(null);
        load();
        return null;
    }

    public void edit(Newsitem c) {
        newsitemFacade.update(c);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Newsitem) event.getObject()).getTitle() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Newsitem) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Newsitem) event.getObject()).getTitle() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String goEdit(int id, String content, String title, Date date) {

        newsitemTemp.setTitle(title);
        newsitemTemp.setContent(content);
        newsitemTemp.setId(id);
        newsitemTemp.setDatepublished(date);
        return "EditNewsContent";
    }

    public String saveChanges() {
        //clear bean to reset new newsitem form
        newsitemBean.setContent(null);
        newsitemBean.setTitle(null);
        edit(newsitemTemp);
        return "news";
    }
}

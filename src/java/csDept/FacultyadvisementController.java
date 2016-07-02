package csDept;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;

@Named(value = "facultyadvisementController")
@SessionScoped
public class FacultyadvisementController implements Serializable {

    @EJB
    FacultyadvisementFacade facultyadvisementFacade;

    @Inject
    FacultyadvisementBean facultyadvisementBean;

    List<Facultyadvisement> facultyadvisements;

    Facultyadvisement facultyadvisementTemp;

    @PostConstruct
    public void init() {
        load();
        facultyadvisementTemp = new Facultyadvisement();
    }

    public void load() {
        this.facultyadvisements = facultyadvisementFacade.findAll();
    }

    public List<Facultyadvisement> getFacultyadvisement() {
        return facultyadvisements;
    }
    
    
    public Facultyadvisement getFacultyadvisementTemp() {
        return facultyadvisementTemp;
    }

    public void setFacultyadvisementTemp(Facultyadvisement facultyadvisementTemp) {
        this.facultyadvisementTemp = facultyadvisementTemp;
    }


    public void setFacultyadvisement(List<Facultyadvisement> facultyadvisements) {
        this.facultyadvisements = facultyadvisements;
    }

    public List<Facultyadvisement> getAll() {
        return facultyadvisementFacade.findAll();
    }

    public int count() {
        return facultyadvisementFacade.count();
    }

    public void delete(Facultyadvisement c) {
        facultyadvisementFacade.remove(c);
        load();
    }

    public String add() {

        Facultyadvisement c = new Facultyadvisement();
        c.setTitle(facultyadvisementBean.getTitle());
        c.setContent(facultyadvisementBean.getContent());
        facultyadvisementFacade.create(c);
        load();
        return null;
    }

    public void edit(Facultyadvisement c) {
        facultyadvisementFacade.update(c);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Facultyadvisement) event.getObject()).getTitle() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Facultyadvisement) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Facultyadvisement) event.getObject()).getTitle() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public String goEdit(int id, String content, String title) {

        facultyadvisementTemp.setTitle(title);
        facultyadvisementTemp.setContent(content);
        facultyadvisementTemp.setId(id);
        return "EditAdvisementContent";
    }

    public String saveChanges() {
        //clear bean to reset new newsitem form
        facultyadvisementBean.setContent(null);
        facultyadvisementBean.setTitle(null);
        edit(facultyadvisementTemp);
        return "academics";
    }
}

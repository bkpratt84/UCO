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
    FacultyAdvisementRepository facultyAdvisementRepository;

    @Inject
    FacultyAdvisementBean facultyAdvisementBean;

    List<FacultyAdvisement> facultyAdvisements;

    FacultyAdvisement facultyAdvisementTemp;

    @PostConstruct
    public void init() {
        load();
        facultyAdvisementTemp = new FacultyAdvisement();
    }

    public void load() {
        this.facultyAdvisements = facultyAdvisementRepository.findAll();
    }

    public List<FacultyAdvisement> getFacultyadvisement() {
        return facultyAdvisements;
    }
    
    
    public FacultyAdvisement getFacultyadvisementTemp() {
        return facultyAdvisementTemp;
    }

    public void setFacultyadvisementTemp(FacultyAdvisement facultyadvisementTemp) {
        this.facultyAdvisementTemp = facultyadvisementTemp;
    }


    public void setFacultyadvisement(List<FacultyAdvisement> facultyadvisements) {
        this.facultyAdvisements = facultyadvisements;
    }

    public List<FacultyAdvisement> getAll() {
        return facultyAdvisementRepository.findAll();
    }

    public int count() {
        return facultyAdvisementRepository.count();
    }

    public void delete(FacultyAdvisement c) {
        facultyAdvisementRepository.remove(c);
        load();
    }

    public String add() {

        FacultyAdvisement c = new FacultyAdvisement();
        c.setTitle(facultyAdvisementBean.getTitle());
        c.setContent(facultyAdvisementBean.getContent());
        facultyAdvisementRepository.create(c);
        load();
        return null;
    }

    public void edit(FacultyAdvisement c) {
        facultyAdvisementRepository.update(c);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((FacultyAdvisement) event.getObject()).getTitle() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((FacultyAdvisement) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((FacultyAdvisement) event.getObject()).getTitle() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public String goEdit(int id, String content, String title) {

        facultyAdvisementTemp.setTitle(title);
        facultyAdvisementTemp.setContent(content);
        facultyAdvisementTemp.setId(id);
        return "EditAdvisementContent";
    }

    public String saveChanges() {
        //clear bean to reset new newsitem form
        facultyAdvisementBean.setContent(null);
        facultyAdvisementBean.setTitle(null);
        edit(facultyAdvisementTemp);
        return "academics";
    }
}

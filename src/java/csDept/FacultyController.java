package csDept;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import registration.User;

@Named(value = "facultyController")
@SessionScoped
public class FacultyController implements Serializable {

    @EJB
    FacultyRepository facultyFacade;

    @Inject
    FacultyBean facultyBean;

    List<Faculty> FTfaculty;
    List<Faculty> Adjunctfaculty;
    List<Faculty> Formerfaculty;
    List<Faculty> Adminfaculty;

    @PostConstruct
    public void init() {
        loadAll();
    }

    public List<Faculty> getFTfaculty() {
        return FTfaculty;
    }

    public void setFTfaculty(List<Faculty> FTfaculty) {
        this.FTfaculty = FTfaculty;
    }

    public List<Faculty> getAdjunctfaculty() {
        return Adjunctfaculty;
    }

    public void setAdjunctfaculty(List<Faculty> Adjunctfaculty) {
        this.Adjunctfaculty = Adjunctfaculty;
    }

    public List<Faculty> getFormerfaculty() {
        return Formerfaculty;
    }

    public void setFormerfaculty(List<Faculty> Formerfaculty) {
        this.Formerfaculty = Formerfaculty;
    }

    public List<Faculty> getAdminfaculty() {
        return Adminfaculty;
    }

    public void setAdminfaculty(List<Faculty> Adminfaculty) {
        this.Adminfaculty = Adminfaculty;
    }

    public void loadFTFaculty() {
        this.FTfaculty = facultyFacade.findByStatus("fulltime");
    }

    public void loadAdjunctFaculty() {
        this.Adjunctfaculty = facultyFacade.findByStatus("adjunct");
    }

    public void loadFormerFaculty() {
        this.Formerfaculty = facultyFacade.findByStatus("former");
    }

    public void loadAdminFaculty() {
        this.Adminfaculty = facultyFacade.findByStatus("administrative");
    }

    public void loadAll() {
        loadFTFaculty();
        loadAdjunctFaculty();
        loadFormerFaculty();
        loadAdminFaculty();
    }

    public List<Faculty> getAll() {
        return facultyFacade.findAll();
    }

    public List<Faculty> findByStatus(String status) {
        return facultyFacade.findByStatus(status);
    }

    public int count() {
        return facultyFacade.count();
    }

    public void delete(Faculty f) {
        facultyFacade.remove(f);
        //replace with if tree (check facultytype) to improve performance
        loadAll();
    }

    public void add() {

        Faculty f = new Faculty();

        f.setEmail(facultyBean.getEmail());
        f.setFirstname(facultyBean.getFirstname());
        f.setJobtitle(facultyBean.getJobtitle());
        f.setLastname(facultyBean.getLastname());
        f.setNameprefix(facultyBean.getNameprefix());
        f.setOffice(facultyBean.getOffice());
        f.setPhone(facultyBean.getPhone());
        f.setWebsite(facultyBean.getWebsite());
        f.setStatus(facultyBean.getStatus());
        facultyFacade.create(f);
        loadAll();
    }

    public void edit(Faculty f) {
        facultyFacade.update(f);
        loadAll();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Faculty) event.getObject()).getLastname() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Faculty) event.getObject());
        loadAll();

    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Faculty) event.getObject()).getLastname() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}

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

@Named(value = "facultyController")
@SessionScoped
public class FacultyController implements Serializable {

    @EJB
    FacultyRepository facultyRepository;

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
        this.FTfaculty = facultyRepository.findByStatus("fulltime");
    }

    public void loadAdjunctFaculty() {
        this.Adjunctfaculty = facultyRepository.findByStatus("adjunct");
    }

    public void loadFormerFaculty() {
        this.Formerfaculty = facultyRepository.findByStatus("former");
    }

    public void loadAdminFaculty() {
        this.Adminfaculty = facultyRepository.findByStatus("administrative");
    }

    public void loadAll() {
        loadFTFaculty();
        loadAdjunctFaculty();
        loadFormerFaculty();
        loadAdminFaculty();
    }

    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findByStatus(String status) {
        return facultyRepository.findByStatus(status);
    }

    public int count() {
        return facultyRepository.count();
    }

    public void delete(Faculty f) {
        facultyRepository.remove(f);
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
        facultyRepository.create(f);
        loadAll();
    }

    public void edit(Faculty f) {
        facultyRepository.update(f);
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

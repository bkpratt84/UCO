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

@Named(value = "degreesController")
@SessionScoped
public class DegreesController implements Serializable {

    @EJB
    DegreesRepository degreeFacade;

    @Inject
    DegreesBean degreeBean;

    List<Degrees> degrees;

    Degrees degreeTemp;

    @PostConstruct
    public void init() {
        load();
        degreeTemp = new Degrees();
    }

    public List<Degrees> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degrees> degrees) {
        this.degrees = degrees;
    }

    public Degrees getDegreeTemp() {
        return degreeTemp;
    }

    public void setDegreeTemp(Degrees degreeTemp) {
        this.degreeTemp = degreeTemp;
    }

    public void load() {
        this.degrees = degreeFacade.findAll();

    }

    public List<Degrees> getAll() {
        return degreeFacade.findAll();
    }

    public int count() {
        return degreeFacade.count();
    }

    public void delete(Degrees b) {
        degreeFacade.remove(b);
        load();
    }

    public String add() {

        Degrees d = new Degrees();
        d.setDegreeName(degreeBean.getDegreeName());
        d.setDegreeCode(degreeBean.getDegreeCode());
        d.setDegreeDesc(degreeBean.getDegreeDesc());
        degreeFacade.create(d);
        load();
        return null;
    }

    public void edit(Degrees d) {

        degreeFacade.update(d);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Degrees) event.getObject()).getDegreeName() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Degrees) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Degrees) event.getObject()).getDegreeName() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String goEdit(int id, String description, String name, String code) {

        degreeTemp.setDegreeName(name);
        degreeTemp.setDegreeDesc(description);
        degreeTemp.setDegreeId(id);
        degreeTemp.setDegreeCode(code);
        return "EditDegreeDescription";
    }

    public String saveChanges() {
        //clear bean to reset new degree form
        degreeBean.setDegreeDesc(null);
        degreeBean.setDegreeName(null);
        degreeBean.setDegreeCode(null);
        edit(degreeTemp);
        return "academics";
    }
}

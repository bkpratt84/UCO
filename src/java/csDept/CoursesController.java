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

@Named(value = "coursesController")
@SessionScoped
public class CoursesController implements Serializable {

    @EJB
    CoursesRepository courseFacade;

    @Inject
    CoursesBean courseBean;

    List<Courses> courses;

    @PostConstruct
    public void init() {
        load();
    }

    public void load() {
        this.courses = courseFacade.findAll();
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Courses> getAll() {
        return courseFacade.findAll();
    }

    public int count() {
        return courseFacade.count();
    }

    public void delete(Courses c) {
        courseFacade.remove(c);
        load();
        //return null;
    }

    public String add() {

        Courses c = new Courses();
        c.setCourseName(courseBean.getCourseName());
        c.setCourseCrn(courseBean.getCourseCrn());
        c.setCourseDesc(courseBean.getCourseDesc());
        courseFacade.create(c);
        load();
        return null;
    }

    public void edit(Courses c) {
        courseFacade.update(c);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Courses) event.getObject()).getCourseName() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Courses) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Courses) event.getObject()).getCourseName() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}

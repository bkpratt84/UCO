package csDept;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;

@Named(value = "resourceController")
@SessionScoped
public class ResourceController implements Serializable {

    @EJB
    ResourceFacade resourceFacade;

    @Inject
    ResourceBean resourceBean;

    List<Resource> resources;

    Resource resourceTemp;

    @PostConstruct
    public void init() {
        load();
        resourceTemp = new Resource();

    }

    public void load() {
        this.resources = resourceFacade.findAll();
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Resource getResourceTemp() {
        return resourceTemp;
    }

    public void setResourceTemp(Resource resourceTemp) {
        this.resourceTemp = resourceTemp;
    }

    public Resource findById(int id) {
        return resourceFacade.findByResourceId(id);
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Resource> getAll() {
        return resourceFacade.findAll();
    }

    public int count() {
        return resourceFacade.count();
    }

    public void delete(Resource c) {
        resourceFacade.remove(c);
        load();
        //return null;
    }

    public String add() {

        Resource c = new Resource();
        c.setTitle(resourceBean.getTitle());
        resourceFacade.create(c);
        resourceBean.setTitle(null);
        load();
        return null;
    }

    public void edit(Resource c) {
        resourceFacade.update(c);
        load();
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Resource) event.getObject()).getTitle() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        edit((Resource) event.getObject());
        load();
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Resource) event.getObject()).getTitle() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String goEdit(int id, String title, List<Resourceitem> items) {
        resourceTemp.setTitle(title);
        resourceTemp.setResourceid(id);
        resourceTemp.setResourceitemCollection(items);
        //also set bean vars for lookup in resourceitemcontroller
        resourceBean.setResourceid(id);
        resourceBean.setResourceitemCollection(items);
        resourceBean.setTitle(title);
        return "EditResourceItems";
    }

    public String saveChanges() {
        //clear bean to reset new form
        resourceBean.setTitle(null);
        edit(resourceTemp);
        return "resources";
    }

}

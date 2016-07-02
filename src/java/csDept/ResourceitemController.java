package csDept;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named(value = "resourceItemController")
@SessionScoped
public class ResourceitemController implements Serializable {
    
    @EJB
    ResourceitemFacade resourceitemFacade;
    
    @EJB
    ResourceFacade resourceFacade;
    
    @Inject
    ResourceitemBean resourceItemBean;
    
    @Inject
    ResourceBean resourceBean;
    
    List<Resourceitem> resourceItems;
    
    Resourceitem resourceItemTemp;
    
    ResourceController resourceController;
    
    @PostConstruct
    public void init() {
        load();
        resourceItemTemp = new Resourceitem();
        
    }
    
    public void load() {
        this.resourceItems = resourceitemFacade.findAll();
    }
    
    public List<Resourceitem> getResourceItems() {
        return resourceItems;
    }
    
    public Resourceitem getResourceItemTemp() {
        return resourceItemTemp;
    }
    
    public void setResourceItemTemp(Resourceitem resourceItemTemp) {
        this.resourceItemTemp = resourceItemTemp;
    }
    
    public Resourceitem findById(int id) {
        return resourceitemFacade.find(id);
    }
    
    public void setResource(List<Resourceitem> resourceItems) {
        this.resourceItems = resourceItems;
    }
    
    public List<Resourceitem> getAll() {
        return resourceitemFacade.findAll();
    }
    
    public int count() {
        return resourceitemFacade.count();
    }
    
    public void delete(Resourceitem c) {
        Resource resource = new Resource();
        resourceitemFacade.remove(c);
        List<Resourceitem> list = resourceBean.getResourceitemCollection();
        list.remove(c);
        resource.setResourceid(resourceBean.getResourceid());
        resource.setTitle(resourceBean.getTitle());
        resource.setResourceitemCollection(list);
        resourceFacade.update(resource);
        load();
        //return null;
    }
    
    public String add() {
        
        Resourceitem item = new Resourceitem();
        Resource resource = new Resource();
        //collect title of new item from form
        item.setTitle(resourceItemBean.getTitle());
        //collect id from selected resource
        resource.setResourceid(resourceBean.getResourceid());
        //add new item to resource
        List<Resourceitem> list = resourceBean.getResourceitemCollection();
        list.add(item);
        //update resource to persist properties
        resource.setTitle(resourceBean.getTitle());
        resource.setResourceitemCollection(list);
        item.setResourceid(resource);
        
        item.setContents(resourceItemBean.getContents());
        resourceitemFacade.create(item);
        resourceFacade.update(resource);
        resourceItemBean.setTitle(null);
        resourceItemBean.setContents(null);
        
        load();
        return null;
    }
    
    public void edit(Resourceitem c) {
        resourceitemFacade.update(c);
        load();
    }
    
    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((Resourceitem) event.getObject()).getTitle() + "\"" + " Was modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        edit((Resourceitem) event.getObject());
        load();
    }
    
    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((Resourceitem) event.getObject()).getTitle() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String goEdit(Resource resourceid, String content, String title, int id) {
        resourceItemTemp.setTitle(title);
        resourceItemTemp.setContents(content);
        resourceItemTemp.setResourceid(resourceid);
        resourceItemTemp.setItemid(id);
        return "EditResourceItemContent";
    }
    
    public String saveChanges() {
        //clear beans to reset new forms
        resourceItemBean.setTitle(null);
        resourceItemBean.setContents(null);
        resourceBean.setTitle(null);

        Resourceitem r = findById(resourceItemTemp.getItemid());
        r.setContents(resourceItemTemp.getContents());
        r.setTitle(resourceItemTemp.getTitle());
        r.setResourceid(resourceItemTemp.getResourceid());
        edit(r);
        return "/resources.xhtml";
    }
    
}

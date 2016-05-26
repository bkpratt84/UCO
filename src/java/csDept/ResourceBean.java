package csDept;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Named(value = "resourceBean")
@SessionScoped
public class ResourceBean implements Serializable {

    private Integer resourceid;
    private String title;
    private List<Resourceitem> resourceitemCollection;

    public Integer getResourceid() {
        return resourceid;
    }

    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Resourceitem> getResourceitemCollection() {
        return resourceitemCollection;
    }

    public void setResourceitemCollection(List<Resourceitem> resourceitemCollection) {
        this.resourceitemCollection = resourceitemCollection;
    }

}

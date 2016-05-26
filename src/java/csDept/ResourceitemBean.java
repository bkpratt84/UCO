package csDept;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "resourceItemBean")
@SessionScoped
public class ResourceitemBean implements Serializable {
    private Integer itemid;
    private String title;
    private String contents;
    private Resource resourceid;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Resource getResourceid() {
        return resourceid;
    }

    public void setResourceid(Resource resourceid) {
        this.resourceid = resourceid;
    }

}

package csDept;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;

@Named(value = "newsitemBean")
@SessionScoped
public class NewsitemBean implements Serializable {

    private Integer id;
    private String title;
    private String content;
    private Date datepublished;

    public Date getDatepublished() {
        return datepublished;
    }

    public void setDatepublished(Date datepublished) {
        this.datepublished = datepublished;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

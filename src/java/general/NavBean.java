
package general;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named

public class NavBean implements Serializable {

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    
    public void chat(){
        setPage("chat");
    }
    
    public void textbooks(){
        setPage("textbooks");
    }
    public void login(){
        setPage("login");
    }
    public void sRegister(){
        setPage("studentRegister");
    }
   
    public void error(){
        setPage("403");
    }
    
    public void loginerror(){
        setPage("loginerror");
    }
    private String page = "index";

}

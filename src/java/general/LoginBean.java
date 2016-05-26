package general;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    public String logOut() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        context.redirect("/sdd-project/faces/index.xhtml");
        return "";
    }

    private Object getFromSession(String attributeName) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        return ctx.getExternalContext().getSessionMap().get(attributeName);
    }

    private void putIntoSession(String attributeName, Object value) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        ctx.getExternalContext().getSessionMap().put(attributeName, value);
    }
}

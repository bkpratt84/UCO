package announcements.utility;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Messages {
    public static void setErrorMessage(String message, String component) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(component, facesMsg);
   }
    
    public static void setErrorMessage(String message) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
   }
    
     public static void setSuccessMessage(String message) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
   } 
}
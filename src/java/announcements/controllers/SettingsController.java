package announcements.controllers;

import announcements.domain.Category;
import announcements.domain.CategoryFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.Size;

@Named(value = "settingsController")
@ViewScoped
public class SettingsController implements Serializable {

    @EJB
    private CategoryFacade categoryFacade;
    
    private List<Category> categories;
    
    @Size(min = 1, max = 25, message = "Between 1-25 chars")
    private String txtCategory;
    private String colorCode;
    private boolean inactive;
    
    private Category category;
    
    @PostConstruct
    public void init() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        
        categories = categoryFacade.findAll();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public String getTxtCategory() {
        return txtCategory;
    }

    public void setTxtCategory(String txtCategory) {
        this.txtCategory = txtCategory;
    }
}
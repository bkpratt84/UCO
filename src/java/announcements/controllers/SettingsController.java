package announcements.controllers;

import announcements.domain.Category;
import announcements.domain.CategoryRepository;
import announcements.utility.Messages;
import java.io.IOException;
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
    private CategoryRepository categoryRepo;
    
    private List<Category> categories;
    
    @Size(min = 1, max = 25, message = "Between 1-25 chars")
    private String txtCategory;
    private String colorCode;
    private boolean inactive;
    private Integer id;
    
    private boolean edit;
    private Category category;
    
    @PostConstruct
    public void init() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        
        txtCategory = null;
        colorCode = "000dff";
        inactive = false;
        id = null;
        edit = false;
        
        categories = categoryRepo.findAll();
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

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
    public void save() throws IOException {
        Category c = new Category();
        
        c.setCategory(txtCategory);
        c.setColorCode(colorCode);
        c.setInactive(inactive);
        
        if (id != null && edit) {
            c.setCategoryID(id);
        }
        
        categoryRepo.createOrUpdate(c, id);
        
        if (edit) {
            Messages.setSuccessMessage("Changes saved.");
        } else {
            Messages.setSuccessMessage("Category added.");
        }

        init();
    }
    
    public void setEditItem(Category c) {
        this.edit = true;
        this.id = c.getCategoryID();
        this.txtCategory = c.getCategory();
        this.colorCode = c.getColorCode();
        this.inactive = c.isInactive();
    }
    
    public void cancel() {
        init();
    }
}
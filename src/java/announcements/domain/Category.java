package announcements.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c WHERE c.inactive = false"),
    @NamedQuery(name = "Category.findByCategoryID", query = "SELECT c FROM Category c WHERE c.categoryID = :categoryID")})
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "categoryID")
    private int categoryID;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "colorCode")
    private String colorCode;
    
    @Column(name = "inactive")
    private boolean inactive;
    
    @OneToMany(mappedBy = "category")
    private List<Post> posts;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.categoryID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.colorCode, other.colorCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Category{" + "categoryID=" + categoryID + ", category=" + category + ", colorCode=" + colorCode + ", inactive=" + inactive + '}';
    }
}
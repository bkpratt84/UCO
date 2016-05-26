package registration;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;


@Named
@SessionScoped
public class ProfileEditing implements Serializable {

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String currentPassword;
    private String oldPassword;
    private String newPassword;
    private String repeatPassword;

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void userInfo() throws IOException, SQLException {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        this.email = request.getRemoteUser();

        Connection conn = ds.getConnection();
        try {
            PreparedStatement getValues = conn.prepareStatement("Select * From USER_INFO WHERE LOWER(email) = ? ");
            getValues.setString(1, email);
            ResultSet result = getValues.executeQuery();
            if (result.next()) {
                setFirstName(result.getString("FIRSTNAME"));
                setLastName(result.getString("LASTNAME"));
                setEmail(result.getString("EMAIL"));
                setUsername(result.getString("USERNAME"));
            }
        } finally {
            conn.close();
        }

        if (context.isUserInRole("instructor")) {
            context.redirect("/sdd-project/faces/instructors/instructorProfile.xhtml");
        } else if (context.isUserInRole("student")) {
            context.redirect("/sdd-project/faces/students/studentProfile.xhtml");
        }
    }

    public void saveProfileUpdate() throws IOException, SQLException {
        Registration reg = new Registration();
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        boolean commited = false;
        try {
            PreparedStatement updatingUser = conn.prepareStatement("UPDATE USER_INFO set lastName=?, firstName=?, email=?, username=? where email=?");
            updatingUser.setString(1, lastName);
            updatingUser.setString(2, firstName);
            updatingUser.setString(3, email);
            updatingUser.setString(4, email);
            updatingUser.setString(5, email);
            updatingUser.executeUpdate();
            conn.commit();
            commited = true;
            if (!commited) {
                conn.rollback();
            }
        } finally {
            conn.close();
        }

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        FacesContext cont = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        message.setSummary("Record Successfully Updated!");
        message.setDetail("Record Successfully Updated");
        cont.addMessage("mform:user", message);
    }

    public void savePasswordUpdate() throws IOException, SQLException {
        Registration reg = new Registration();

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        this.email = request.getRemoteUser();

        Connection connGet = ds.getConnection();
        try {
            PreparedStatement getValues = connGet.prepareStatement("Select * From USER_INFO WHERE LOWER(email) = ? ");
            getValues.setString(1, email);
            ResultSet result = getValues.executeQuery();
            if (result.next()) {
                currentPassword = result.getString("PASSWORD");
            }
        } finally {
            connGet.close();
        }

        if (!(currentPassword.equals(reg.encryptPassword(oldPassword)))) {
            FacesContext cont = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage();
            message.setSummary("Wrong Old Password!");
            message.setDetail("Wrong Old Password!");
            cont.addMessage("mform:user", message);
        } else if (!(newPassword.equals(repeatPassword))) {
            FacesContext cont = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage();
            message.setSummary("Password Mismatch!");
            message.setDetail("Password Mismatch!");
            cont.addMessage("mform:user", message);
        } else if ((currentPassword.equals(reg.encryptPassword(oldPassword))) && (newPassword.equals(repeatPassword))) {

            Connection connUpdate = ds.getConnection();
            connUpdate.setAutoCommit(false);
            boolean commited = false;
            try {
                PreparedStatement updatingUser = connUpdate.prepareStatement("UPDATE USER_INFO set password=? where email=?");
                updatingUser.setString(1, reg.encryptPassword(newPassword));
                updatingUser.setString(2, email);
                updatingUser.executeUpdate();
                connUpdate.commit();
                commited = true;
                if (!commited) {
                    connUpdate.rollback();
                }
            } finally {
                connUpdate.close();
            }

            FacesContext cont = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage();
            message.setSummary("Record Successfully Updated!");
            message.setDetail("Record Successfully Updated");
            cont.addMessage("mform:user", message);
        }

    }
}

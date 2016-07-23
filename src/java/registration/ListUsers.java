package registration;

import csDept.User;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.sql.DataSource;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

@Named
@SessionScoped
public class ListUsers implements Serializable {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;

    private List<User> list;
    ArrayList<User> userList = new ArrayList<User>();

    private Boolean flag = false;
    private Boolean deletedFlag = false;

    Registration reg = new Registration();

    private DataTable datatableUser;

    public DataTable getDatatableUser() {
        return datatableUser;
    }

    public void setDatatableUser(DataTable datatableUser) {
        this.datatableUser = datatableUser;
    }

    public List<User> getList() throws SQLException {
        list = listSystemUsers();
        return list;
    }

    public ArrayList<User> listSystemUsers() throws SQLException {
        if (flag == false || deletedFlag == true) {
            Connection conn = ds.getConnection();
            try {
                String query = "Select * from USER_INFO";;
                PreparedStatement checkQuery = conn.prepareStatement(query);
                ResultSet result = checkQuery.executeQuery();
                userList = new ArrayList<User>();
                while (result.next()) {
                    int ID = result.getInt("ID");
                    String username = result.getString("USERNAME");
                    String firstName = result.getString("FIRSTNAME");
                    String lastName = result.getString("LASTNAME");
                    String email = result.getString("EMAIL");
                    Boolean active = result.getBoolean("ACTIVE");

                    User new_user = new User(ID, username, firstName, lastName, email, active);

                    userList.add(new_user);
                    flag = true;
                    deletedFlag = false;
                }
                return userList;
            } finally {
                conn.close();
            }
        }
        return userList;
    }

    public void deleteUser(Integer ID, String username) throws SQLException {

        Connection conn = ds.getConnection();
        try {
            PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM USER_INFO where ID=?");
            deleteUser.setInt(1, ID);
            deleteUser.executeUpdate();
            PreparedStatement deleteUserGroup = conn.prepareStatement("DELETE FROM USER_GROUPS where USERNAME=?");
            deleteUserGroup.setString(1, username);
            deleteUserGroup.executeUpdate();
            PreparedStatement deleteUserStudent = conn.prepareStatement("DELETE FROM STUDENT where USERNAME=?");
            deleteUserStudent.setString(1, username);
            deleteUserStudent.executeUpdate();
            conn.commit();
            deletedFlag = true;

        } finally {
            conn.close();
        }
    }

    public void onEdit(RowEditEvent event) throws SQLException {
        FacesMessage msg = new FacesMessage("Record Updated", "\"" + ((User) event.getObject()).getUsername() + "\"" + " Was modified!");
        Connection conn = ds.getConnection();

        try {
            conn.setAutoCommit(false);
            boolean commited = false;
            try {
                PreparedStatement updatingUser = conn.prepareStatement("UPDATE USER_INFO set firstName=?, lastName=?,email=?, active=? where username=?");
                updatingUser.setString(1, ((User) event.getObject()).getFirstName());
                updatingUser.setString(2, ((User) event.getObject()).getLastName());
                updatingUser.setString(3, ((User) event.getObject()).getEmail());
                updatingUser.setBoolean(4, ((User) event.getObject()).isActive());
                updatingUser.setString(5, ((User) event.getObject()).getUsername());
                updatingUser.executeUpdate();
                conn.commit();
                commited = true;
            } finally {
                if (!commited) {
                    conn.rollback();
                }
            }
        } finally {
            conn.close();
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Update Cancelled", "\"" + ((User) event.getObject()).getUsername() + "\"" + " Was not modified!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}

package csDept;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named
@SessionScoped
public class AdminEnrollBean implements Serializable {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    private boolean enabled;
    private boolean active;
    private int sid;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArrayList<Student> getStudents() throws SQLException {
        ArrayList<Student> list = new ArrayList<>();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        try (Connection conn = ds.getConnection()) {

            String query = "SELECT * FROM STUDENT";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Student c = new Student();
                    c.setId(rs.getInt("STUDENT_ID"));
                    c.setUsername(rs.getString("USERNAME"));
                    c.setFirstname(rs.getString("FIRSTNAME"));
                    c.setLastname(rs.getString("LASTNAME"));
                    c.setActive(rs.getString("ACTIVE"));
                    list.add(c);
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public void enableEnroll(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE STUDENT SET ACTIVE =?  WHERE STUDENT_ID=" + id;

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Y");
            ps.executeUpdate();
            System.out.println(id);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void disableEnroll(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE STUDENT SET ACTIVE = ? WHERE STUDENT_ID=" + id;

        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "N");
            ps.executeUpdate();
            System.out.println(id);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean getActiveButton() throws SQLException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
         HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        boolean bButton = false;
        String sButton = null;
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "SELECT ACTIVE FROM STUDENT WHERE USERNAME=?";
        
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               sButton = rs.getString("ACTIVE");
               bButton = sButton.equalsIgnoreCase("Y");
            }
            ps.close();
            conn.close();
        
        System.out.println(sButton+" "+bButton+" "+username);
        return bButton;

    }
}

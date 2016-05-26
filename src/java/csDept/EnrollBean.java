package csDept;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
public class EnrollBean implements Serializable {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    int cid;
    int sid;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public ArrayList<Course> getEnrolledCourse() throws SQLException {
       ArrayList<Course> list = new ArrayList<>();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        try (Connection conn = ds.getConnection()) {

            String query = "SELECT COURSES.COURSE_ID,COURSES.COURSE_NAME, COURSES.COURSE_CRN FROM ENROLL,COURSES,USER_INFO WHERE USER_INFO.USERNAME=? AND COURSES.COURSE_ID = COURSEID";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("COURSE_ID"));
                c.setCrn(rs.getString("COURSE_CRN"));
                c.setName(rs.getString("COURSE_NAME"));
                list.add(c);
            }
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public ArrayList<Course> getTeachCourse() throws SQLException {
        ArrayList<Course> list = new ArrayList<>();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        try (Connection conn = ds.getConnection()) {

            String query = "SELECT COURSES.COURSE_ID,COURSES.COURSE_NAME, COURSES.COURSE_CRN FROM TEACHING,COURSES,USER_INFO WHERE USER_INFO.USERNAME=? AND COURSES.COURSE_ID = COURSEID";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("COURSE_ID"));
                c.setCrn(rs.getString("COURSE_CRN"));
                c.setName(rs.getString("COURSE_NAME"));
                list.add(c);
            }
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public String addCourse(int id) throws SQLException {
       ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();

        if (ds == null) {
            return "db_connection_error";
        }

        Connection conn;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            return "db_connection_error";
        }

        int count = 0;
        try {
            PreparedStatement countQuery = conn.prepareStatement(
                    "SELECT COUNT(*) AS total from ENROLL WHERE COURSEID = ?");
            countQuery.setInt(1, id);
            ResultSet rs = countQuery.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                return "db_connection_error";
            }
        }
        if (count != 0) {
            System.out.println("Already Exist");
            return null;
        } else {
            try {
                String qr = "SELECT ID FROM USER_INFO WHERE USERNAME = ?";
                conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(qr);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    setSid(rs.getInt("ID"));
                }
                String insertQuery = "INSERT INTO ENROLL (STUDENTID, COURSEID) VALUES (?,?)";
                PreparedStatement insStmt = conn.prepareStatement(insertQuery);
                insStmt.setInt(1, getSid());
                insStmt.setInt(2, id);
                System.out.println("==========  records = " + getSid());
                insStmt.executeUpdate();
                insStmt.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public String addTeach(int id) throws SQLException {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();

        if (ds == null) {
            return "db_connection_error";
        }

        Connection conn;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            return "db_connection_error";
        }

        int count = 0;
        try {
            PreparedStatement countQuery = conn.prepareStatement(
                    "SELECT COUNT(*) AS total from TEACHING WHERE COURSEID = ?");
            countQuery.setInt(1, id);
            ResultSet rs = countQuery.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                return "db_connection_error";
            }
        }
        if (count != 0) {
            System.out.println("Already Exist");
            return null;
        } else {
            try {
                String qr = "SELECT ID FROM USER_INFO WHERE USERNAME = ?";
                conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(qr);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    setSid(rs.getInt("ID"));
                }
                String insertQuery = "INSERT INTO TEACHING (FACULTYID, COURSEID) VALUES (?,?)";
                PreparedStatement insStmt = conn.prepareStatement(insertQuery);
                insStmt.setInt(1, getSid());
                insStmt.setInt(2, id);
                System.out.println("==========  records = " + id);
                insStmt.executeUpdate();
                insStmt.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public void deleteEnroll(int id) throws SQLException {
        Statement stmt = null;
        try (Connection conn = ds.getConnection()) {
            stmt = conn.createStatement();
            String sql = "DELETE FROM ENROLL WHERE COURSEID=" + id;
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            System.out.println("========== deleted records = " + id);
            conn.close();
        }
        FacesMessage msg = new FacesMessage("Course dropped");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteTeach(int id) throws SQLException {
        Statement stmt = null;
        try (Connection conn = ds.getConnection()) {
            stmt = conn.createStatement();
            String sql = "DELETE FROM TEACHING WHERE COURSEID=" + id;
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            System.out.println("========== deleted records = " + id);
            conn.close();
        }
        FacesMessage msg = new FacesMessage("Course dropped");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public ArrayList<Course> getCourse() throws SQLException {
        ArrayList<Course> list = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {

            String query = "SELECT * FROM COURSES";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("COURSE_ID"));
                c.setCrn(rs.getString("COURSE_CRN"));
                c.setName(rs.getString("COURSE_NAME"));
                list.add(c);
            }
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }
}

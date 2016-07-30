package registration;

import announcements.utility.Utility;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named
@SessionScoped
public class Registration implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
    private Boolean newUser = false;
    private Boolean verifyUser = false;
    private String verificationLink;

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    private SecureRandom random = new SecureRandom();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Boolean isVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Boolean verifyUser) {
        this.verifyUser = verifyUser;
    }

    public Boolean addInstructor() throws SQLException, IOException, MessagingException {

        if (ds == null) {
            throw new SQLException("No Data Source");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("No Connection");
        }

        try {
            conn.setAutoCommit(false);
            boolean commited = false;
            try {
                String query = "SELECT count(*) AS total FROM USER_INFO WHERE LOWER(username) = ?";
                PreparedStatement checkQuery = conn.prepareStatement(query);
                checkQuery.setString(1, email.toLowerCase());
                ResultSet result = checkQuery.executeQuery();
                int cnt = 0;

                password = generatePassword();

                while (result.next()) {
                    cnt = result.getInt("total");

                }
                if (cnt != 0) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage();
                    message.setSummary("User Already Exists");
                    message.setDetail("User Already Exists");
                    context.addMessage("mform:addInstructor", message);
                    return false;
                } else {
                    PreparedStatement insertGroupQuery = conn.prepareStatement(
                            "INSERT INTO USER_GROUPS (GROUPNAME, USERNAME) values (?,?)");
                    insertGroupQuery.setString(1, "instructor");
                    insertGroupQuery.setString(2, email);
                    insertGroupQuery.executeUpdate();

                    PreparedStatement addingInstructor = conn.prepareStatement("INSERT INTO USER_INFO (firstName, lastName, username, password, email, active) VALUES (?, ?, ?, ?, ?, ?)");
                    addingInstructor.setString(1, firstName);
                    addingInstructor.setString(2, lastName);
                    addingInstructor.setString(3, email);
                    addingInstructor.setString(4, encryptPassword(password));
                    addingInstructor.setString(5, email);
                    addingInstructor.setBoolean(6, true);
                    addingInstructor.executeUpdate();
                    conn.commit();
                    commited = true;
                    this.newUser = true;

                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage();
                    message.setSummary("User Created. Email Notification Sent!");
                    message.setDetail("User Created. Email Notification Sent!");
                    context.addMessage("mform:user", message);

                    //Send the email here!
                    GoogleMail m = new GoogleMail();
                    String emailText = "Welcome to the UCO Computer Science!<p/><p/>" + "Your request has been approved and "
                            + "You can now login using the credentials below.<p/>" + "<b>Username:</b> " + email
                            + "<p/><b>Password:</b> " + password
                            + "<p/>Thank you,<p/>" + "Central Soft Team<p/>";
                    
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    
                    GoogleMail.Send(externalContext,
                            email,
                            "Your Account Information",
                            emailText);

                    clearInput();
                }

                return true;

            } finally {
                if (!commited) {
                    conn.rollback();
                }
            }
        } finally {
            conn.close();
        }
    }

    public Boolean addStudent() throws SQLException, IOException, MessagingException {

        if (ds == null) {
            throw new SQLException("No Data Source");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("No Connection");
        }

        try {
            conn.setAutoCommit(false);
            boolean commited = false;
            try {
                String query = "SELECT count(*) AS total FROM USER_INFO WHERE LOWER(username) = ?";
                PreparedStatement checkQuery = conn.prepareStatement(query);
                checkQuery.setString(1, email.toLowerCase());
                ResultSet result = checkQuery.executeQuery();
                int cnt = 0;

                while (result.next()) {
                    cnt = result.getInt("total");

                }
                if (cnt != 0) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage();
                    message.setSummary("User Already Exists");
                    message.setDetail("User Already Exists");
                    context.addMessage("mform:addStudent", message);
                    return false;
                } else if (!password.equals(repeatPassword)) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage();
                    message.setSummary("Password Mismatch!");
                    message.setDetail("Password Mismatch!");
                    context.addMessage("mform:addStudent", message);
                    return false;
                } else {
                    PreparedStatement insertGroupQuery = conn.prepareStatement(
                            "INSERT INTO USER_GROUPS (GROUPNAME, USERNAME) values (?,?)");
                    insertGroupQuery.setString(1, "student");
                    insertGroupQuery.setString(2, email);
                    insertGroupQuery.executeUpdate();

                    PreparedStatement addingStudent = conn.prepareStatement("INSERT INTO USER_INFO (firstName, lastName, username, password, email, active, activationKey) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    addingStudent.setString(1, firstName);
                    addingStudent.setString(2, lastName);
                    addingStudent.setString(3, email);
                    addingStudent.setString(4, encryptPassword(password));
                    addingStudent.setString(5, email);
                    addingStudent.setBoolean(6, false);
                    addingStudent.setString(7, encryptString(email));
                    addingStudent.executeUpdate();

                    PreparedStatement insertStudent = conn.prepareStatement(
                            "INSERT INTO STUDENT (username, firstname,"
                                    + "lastname, active) VALUES (?, ?, ?, ?)");
                    insertStudent.setString(1, email);
                    insertStudent.setString(2, firstName);
                    insertStudent.setString(3, lastName);
                    insertStudent.setString(4, "Y");
                    insertStudent.executeUpdate();
                            
                    conn.commit();
                    commited = true;
                    this.newUser = true;

                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage();
                    message.setSummary("Please check your email to confirm the registration!");
                    message.setDetail("Please check your email to confirm the registration!");
                    context.addMessage("mform:addStudent", message);

                    InetAddress ip;
                    ip = InetAddress.getLocalHost();

                    String currentPos = ip.getHostAddress().toString() + ":8080" + 
                            context.getExternalContext().getRequestContextPath() + 
                            "/faces/accountVerification.xhtml";

                    verificationLink = currentPos + "?id=" + encryptString(email);

                    //Send the email here
                    GoogleMail m = new GoogleMail();
                    String emailText = "Welcome to the UCO Computer Science!<p/><p/>" 
                            + "Please use the link below to verify the account "
                            + "<li>" + verificationLink + "</li>"
                            + "<p/>Thank you,<p/>" + "Central Soft Team<p/>";
                    
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    
                    GoogleMail.Send(externalContext,
                            email,
                            "UCO CS Registration Verification",
                            emailText);
                    

                    clearInput();
                }

                return true;

            } finally {
                if (!commited) {
                    conn.rollback();
                }
            }
        } finally {
            conn.close();
        }
    }

    public String encryptString(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return "encryption error";
    }

    public Boolean approveStudent() throws SQLException, IOException, MessagingException {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        String id = params.get("id");

        if (ds == null) {
            throw new SQLException("No Data Source");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("No Connection");
        }

        conn.setAutoCommit(false);
        boolean commited = false;
        try {
            PreparedStatement updatingUser = 
                    conn.prepareStatement("UPDATE USER_INFO set ACTIVE=? where ACTIVATIONKEY=?");
            updatingUser.setBoolean(1, true);
            updatingUser.setString(2, id);
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
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        context.redirect("/sdd-project/faces/verificationSuccess.xhtml");

        return false;
    }

    public void clearInput() {
        firstName = "";
        lastName = "";
        email = "";
        username = "";
        password = "";

    }

    private String generatePassword() {
        return new BigInteger(130, random).toString(32);
    }

    public String encryptPassword(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return "encryption error";
    }

}
package event;

import announcements.utility.Utility;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import registration.GoogleMail;

@Named(value = "EM")
@SessionScoped
public class StudentEventMgmt implements Serializable{
    
    // CONNECTION TO THE DATABASE-----------------------------------------------
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    //--------------------------------------------------------------------------
    
    
//==============================================================================    
    // LIST THAT HOLD ALL THE PROFFESSORS THAT HAVE ACTIVE EVENTS---------------
    ArrayList<User> profs;
    //--------------------------------------------------------------------------
    
    //LIST OF ALL EVENTS FOR CURRENT INSTRUCTOR---------------------------------
    ArrayList<EventHolder> events;
    //--------------------------------------------------------------------------
    
    //LIST OF ALL OPEN SLOTS FOR CURRENT EVENT----------------------------------
    ArrayList<Slots> slots;
    //--------------------------------------------------------------------------
//==============================================================================
    
//==============================================================================    
    //CURRENT INSTRUCTOR USERNAME-----------------------------------------------
    private String profEmail;
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    private int eventId = 1;
    //--------------------------------------------------------------------------
    
    //CURRENT SLOT ID----------------------------------------------------------
    private int lost_id;
    //--------------------------------------------------------------------------
    
    //SHOW SLOT NOTES TABLE ----------------------------------------------------
    private boolean showComment = false;
    //--------------------------------------------------------------------------
//==============================================================================    

    public void setShowComment(){
        System.out.println("IN SHOW COMMENTS");
        showComment = !showComment;
    }
    
    public boolean getShowComment(){
        return this.showComment;
    }
    
    
    public String setProfEmail(User u){
        this.profEmail = u.getEmail();
        
        System.out.println("JUST SET EMAIL TO: " + this.profEmail);
        
        return "/students/ViewEvents";
    }
    
    public String getProfEmail(){
        return this.profEmail;
    }
    //--------------------------------------------------------------------------
    
    public String setedi(EventHolder eh){
        
        System.out.println("TESING");
        
        this.eventId = eh.getEventId();
        
        return "/students/ViewSlots";
        
    }
    
    public String reserverSlot(int slotId) throws SQLException, MessagingException{
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }
        
        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        String profName = "";
        String eventName = "";
        String startTime = "";
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "UPDATE EVENT_SLOTS SET USER_INFO_USERNAME = ?, USER_INFO_EMAIL = ?, STATUS = 'T' WHERE EVENT_SLOT_ID = ?";

                
                PreparedStatement stmt = conn.prepareStatement(query);  
                stmt.setString(1, username);
                stmt.setString(2, username);
                stmt.setInt(3, slotId);
                
                stmt.executeUpdate();
                
                String infoQuery = "SELECT "
                                    + "EVENT_SLOTS.START_TIME, "
                                    + "EVENTS.EVENT_NAME, "
                                    + "USER_INFO.TITLE, "
                                    + "USER_INFO.LASTNAME "
                                    + "FROM EVENT_SLOTS "
                                    + " JOIN EVENTS ON EVENT_SLOTS.EVENT_ID = EVENTS.EVENT_ID "
                                    + " JOIN USER_INFO ON EVENTS.FACULTY_USERNAME = USER_INFO.USERNAME "
                                    + " WHERE EVENT_SLOTS.EVENT_SLOT_ID = ?";
                
                PreparedStatement infoStmt = conn.prepareStatement(infoQuery);
                
                infoStmt.setInt(1, slotId);
                
                ResultSet rs = infoStmt.executeQuery();
                
                while(rs.next()){
                    profName = rs.getString("TITLE") + rs.getString("LASTNAME");
                    eventName = rs.getString("EVENT_NAME");
                    startTime = timeFormat.format(rs.getTimestamp("START_TIME"));
                }
                
                
                

                conn.commit();
                committed = true;

            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        }finally{    
            conn.close();
        } 
        
        
        GoogleMail m = new GoogleMail();
        
        String flashMessage = "Successfull Registered  or " + eventName + " With " + profName + " at " + startTime;
        
        FacesMessage facesMsg = new FacesMessage(
                FacesMessage.SEVERITY_INFO, flashMessage, null);
        
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        
            
        String title = "UCO: Appointment Reminder";     
        String emailMessage = "<p>Thanks You for Signing up for " + eventName + ".</p>"
                            + "<h3>Just a Reminder of Your Event Details</h3>"
                            + "Event Name: " + eventName + "<br/>"
                            + "Event Time: " + startTime + "<br/>"
                            + "Instructor Name: " + profName + "<br/>";
            
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        GoogleMail.Send(externalContext,
                "barnettlynn@gmail.com",
                title,
                emailMessage);
        
        return "/students/index";
    }
        
      
    public ArrayList<User> getProfWithEvents() throws SQLException{
        
        ArrayList<User> profs = new ArrayList<>();
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }
        
        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT TITLE, LASTNAME, USER_INFO.EMAIL "
                        + "FROM USER_INFO "
                        + "JOIN USER_GROUPS ON USER_INFO.EMAIL = USER_GROUPS.USERNAME "
                        + "WHERE EXISTS (SELECT * FROM EVENTS WHERE EVENTS.FACULTY_EMAIL = USER_INFO.EMAIL) "
                        + "AND USER_GROUPS.GROUPNAME = 'instructor' "
                        + "AND ACTIVE = 'T'";

                PreparedStatement stmt = conn.prepareStatement(query);
                 
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    User u = new User();
                    u.setTitle(rs.getString("TITLE"));
                    u.setLastName(rs.getString("LASTNAME"));                    
                    u.setEmail(rs.getString("EMAIL"));
                    


                    profs.add(u);
                }

                conn.commit();
                committed = true;

            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        }finally{    
            conn.close();
        }   
        
        return profs;
    }
    
    public ArrayList<EventHolder> getEvents() throws SQLException{
       
        events = new ArrayList<>();
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }
        
        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT * FROM EVENTS WHERE ACTIVE = 'T' "
                        + "AND FACULTY_EMAIL = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                
                System.out.println("SETTING PROF EMAIL: " + this.profEmail);
                
                stmt.setString(1, this.profEmail);
                 
                ResultSet rs = stmt.executeQuery();
                
                SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
                while(rs.next()){
                    EventHolder e = new EventHolder();
                    
                    e.setEventName(rs.getString("EVENT_NAME"));
                    e.setEventId(rs.getInt("EVENT_ID"));
                    
;
                    
                    String start = timeFormat.format(rs.getTimestamp("START_TIME"));
                    e.setStartTime(start);
                    
                    String end = timeFormat.format(rs.getTime("END_TIME"));
                    e.setEndTime(end);
                    
                    events.add(e);
                }

                conn.commit();
                committed = true;

            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        }finally{    
            conn.close();
        }   
        

        return events;
        
    }
    
    public ArrayList<Slots> getSlots() throws SQLException{
                
        slots = new ArrayList<>();
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }
        
        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT * FROM EVENT_SLOTS WHERE STATUS = 'O' "
                        + "AND EVENT_ID = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setInt(1, this.eventId);
                 
                ResultSet rs = stmt.executeQuery();
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
                while(rs.next()){
                    Slots s = new Slots();
                    
                    s.setSlotId(rs.getInt("EVENT_SLOT_ID"));
                    
                    String start = timeFormat.format(rs.getTimestamp("START_TIME"));

                    
                    s.setStartTime(start);
      
                    slots.add(s);
                }

                conn.commit();
                committed = true;

            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        }finally{    
            conn.close();
        }   
        
        System.out.println(slots.size());
        
        return slots;
    }
    
    
}

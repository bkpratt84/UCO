package event;

import announcements.utility.Utility;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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

@Named(value = "IEM")
@SessionScoped
public class InstructorEventMgmt implements Serializable{
    
    
    // CONNECTION TO THE DATABASE-----------------------------------------------
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    //--------------------------------------------------------------------------
    
    //PATH VARIABLES------------------------------------------------------------
    private int eventID;
    //--------------------------------------------------------------------------
    
    public ArrayList<Slots> getOpenSlots() throws SQLException{
        ArrayList<Slots> slots = new ArrayList<>();
        
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
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT "
                        + "EVENT_SLOT_ID, "
                        + "EVENT_SLOTS.EVENT_ID, "
                        + "USER_INFO_USERNAME, "
                        + "EVENT_SLOTS.START_TIME, "
                        + "STATUS, "
                        + "EVENTS.EVENT_NAME "
                        + "FROM EVENT_SLOTS "
                        + "JOIN EVENTS ON EVENT_SLOTS.EVENT_ID = EVENTS.EVENT_ID "
                        + "WHERE STATUS = 'O' "
                        + "AND EVENT_SLOTS.EVENT_ID = ? "
                        + "AND EVENTS.FACULTY_USERNAME = ?";

                PreparedStatement stmt = conn.prepareStatement(query);  
                stmt.setInt(1, this.eventID);
                stmt.setString(2, username);
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    Slots s = new Slots();
                    
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
        
        return slots;
    }
    
    public ArrayList<Slots> getTakenSlots() throws SQLException{
        ArrayList<Slots> slots = new ArrayList<>();
        
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
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT "
                        + "EVENT_SLOT_ID, "
                        + "EVENT_SLOTS.EVENT_ID, "
                        + "USER_INFO_USERNAME, "
                        + "EVENT_SLOTS.START_TIME, "
                        + "STATUS, "
                        + "EVENTS.EVENT_NAME, "
                        + "USER_INFO.FIRSTNAME, "
                        + "USER_INFO.LASTNAME, "
                        + "USER_INFO.EMAIL "
                        + "FROM EVENT_SLOTS "
                        + "JOIN EVENTS ON EVENT_SLOTS.EVENT_ID = EVENTS.EVENT_ID "
                        + "JOIN USER_INFO ON USER_INFO_USERNAME = USER_INFO.USERNAME "
                        + "WHERE STATUS = 'T' "
                        + "AND EVENT_SLOTS.EVENT_ID = ? "
                        + "AND FACULTY_USERNAME = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                System.out.println(this.eventID);
                stmt.setInt(1, this.eventID);
                stmt.setString(2, username);
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    Slots s = new Slots();
                    
                    String start = timeFormat.format(rs.getTimestamp("START_TIME"));
                    s.setStartTime(start);
                    s.setStudentName(rs.getString("USER_INFO_USERNAME"));
                    
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
        return slots;
    }
    
    
    public ArrayList<EventHolder> getEvents() throws SQLException{
    
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        
        ArrayList<EventHolder> events = new ArrayList<>();
        
        Connection conn = ds.getConnection();
        try{
            
            String query = "SELECT * FROM EVENTS WHERE FACULTY_USERNAME = ? AND ACTIVE = 'T'";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, username);
            
            ResultSet eventsResults = stmt.executeQuery();
            
            SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
            while(eventsResults.next()){
                EventHolder e = new EventHolder();
                e.setEventName(eventsResults.getString("EVENT_NAME"));
                e.setEventId(eventsResults.getInt("EVENT_ID"));
                
                
                Date tempD = eventsResults.getDate("DAY");
                e.setDay(dayFormat.format(tempD));
                        
                Timestamp tempTS = eventsResults.getTimestamp("START_TIME");
                e.setStartTime(timeFormat.format(tempTS));
                
                tempTS = eventsResults.getTimestamp("END_TIME");
                e.setEndTime(timeFormat.format(tempTS));
                
                events.add(e);
            }
            
        } finally {
            conn.close();
        }
        
        return events;
    }
    
    public String viewEvents(EventHolder eh){
        this.eventID = eh.getEventId();
        return "/instructors/EventDetailView";
    }
    
    public void closeEvent(EventHolder e) throws ParseException, SQLException{
            
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
                String query = "UPDATE EVENTS SET ACTIVE = ? WHERE EVENT_ID = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "F");
                System.out.println("EVENT ID: " + e.getEventId());
                stmt.setInt(2, e.getEventId());
                
                stmt.executeUpdate();

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
        

    
    }
    
    public String cancelEvent(EventHolder e) throws SQLException, MessagingException{
        String profName = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        
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
                
                String profQuery = "SELECT USER_INFO.TITLE, "
                        + "USER_INFO.LASTNAME,"
                        + "EVENTS.EVENT_ID "
                        + "FROM USER_INFO "
                        + "JOIN EVENTS ON EVENTS.FACULTY_USERNAME = USER_INFO.USERNAME "
                        + "WHERE EVENTS.EVENT_ID = ?";
                
                PreparedStatement profStmt = conn.prepareStatement(profQuery);
                profStmt.setInt(1, e.getEventId());
                
                ResultSet prs = profStmt.executeQuery();
                
                while(prs.next()){
                    profName = prs.getString("TITLE") + prs.getString("LASTNAME");
                }
                
                
                
                
                String query = "SELECT "
                                + "USER_INFO.FIRSTNAME, "
                                + "USER_INFO.LASTNAME, "
                                + "USER_INFO.EMAIL, "
                                + "EVENTS.EVENT_NAME, "
                                + "EVENT_SLOTS.START_TIME "
                                + "FROM USER_INFO "
                                + "JOIN EVENTS ON USER_INFO.USERNAME = EVENTS.FACULTY_USERNAME "
                                + "JOIN EVENT_SLOTS ON EVENT_SLOTS.EVENT_ID = EVENTS.EVENT_ID "
                                + "WHERE EVENTS.EVENT_ID = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, e.getEventId());

                ResultSet rs = stmt.executeQuery();
                
                String header = "UCO Appointment Cancelation";
                GoogleMail m = new GoogleMail();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                
                while(rs.next()){
                    String enam;
                    String time;
                    
                    enam = rs.getString("EVENT_NAME");
                    time = timeFormat.format(rs.getTimestamp("START_TIME"));
                    
                    
                    String Message = "<h3>Your appointment for " + enam + " with "
                            + profName + " at " + time + "has been canceled</h3>";
                    
                    GoogleMail.Send(externalContext,
                            "barnettlynn@gmail.com",
                            header,
                            Message);
                }
                
                String eupdate = "UPDATE EVENTS SET ACTIVE='C' WHERE EVENT_ID = ?";
                PreparedStatement eStmt = conn.prepareStatement(eupdate);
                
                eStmt.setInt(1, e.getEventId());
                
                eStmt.executeUpdate();
                

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
        
        FacesMessage facesMsg = new FacesMessage(
        FacesMessage.SEVERITY_INFO, "Event Canceled, All Apointments Have Been Notified", null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        
        return "/instructors/index";
    }    
}

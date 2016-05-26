package event;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named
@RequestScoped
public class EventView {
    
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    private String profUsername;
    private ArrayList<Event> profEvents;
    private ArrayList<Slots> slots;

    public String getProfUsername() {
        return profUsername;
    }

    public void setProfUsername(String profUsername) {
        this.profUsername = profUsername;
    }
    
    
    public ArrayList<User>getProfWithEvent() throws SQLException{
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
                String query;
                query = "SELECT TITLE, LASTNAME, USER_INFO.USERNAME FROM USER_INFO "
                        + "JOIN USER_GROUPS ON USER_INFO.USERNAME = USER_GROUPS.USERNAME "
                        + "WHERE EXISTS (SELECT * FROM EVENTS WHERE EVENTS.FACULTY_USERNAME = USER_INFO.USERNAME) "
                        + "AND USER_GROUPS.GROUPNAME = 'instructor' "
                        + "AND ACTIVE = 'T'";

                 PreparedStatement stmt = conn.prepareStatement(query);
                 

                 
                ResultSet rs = stmt.executeQuery();
                


                while(rs.next()){
                    User u = new User();
                    u.setTitle(rs.getString("TITLE"));
                    u.setLastName(rs.getString("LASTNAME"));
                    u.setUserName(rs.getString("USERNAME"));

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
 
    public void loadEvents() throws SQLException{
        
        profEvents = new ArrayList<>();
        
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
                String query = "SELECT * FROM EVENTS WHERE FACULTY_USERNAME = ? AND ACTIVE = 'T'";

                PreparedStatement stmt = conn.prepareStatement(query);
                 
                stmt.setString(1, this.profUsername);
                 
                ResultSet rs = stmt.executeQuery();
                


                while(rs.next()){
                    
                    SimpleDateFormat dayF = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat timeF = new SimpleDateFormat("hh:mm a");
                    
                    Event e = new Event();
                    e.setEventName(rs.getString("EVENT_NAME"));
                    e.setFacultyUserName(rs.getString("FACULTY_USERNAME"));
                    e.setEventId(rs.getInt("EVENT_ID"));
                    
                    Timestamp day = rs.getTimestamp("DAY");
                    e.setEventDay(dayF.format(day));
                    
                    Timestamp start = rs.getTimestamp("START_TIME");
                    e.setStartTime(timeF.format(start));
                    
                    Timestamp end = rs.getTimestamp("END_TIME");
                    e.setEndTime(timeF.format(end));
                    
                    profEvents.add(e);
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
    }
    
    public ArrayList<Event> getProfEvents(){
        return this.profEvents;
    }
    
    public ArrayList<Slots> getSlots(){
        slots = new ArrayList<>();
        
        
        return slots;
    }
    
}

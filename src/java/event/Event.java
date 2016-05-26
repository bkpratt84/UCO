package event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named(value = "event")
@RequestScoped
public class Event {
    private int     eventId;
    private String  facultyUserName;
    private String  eventName;
    private String  eventDay;
    private String  startTime;
    private String  endTime;
    private int     slotSize;
    private boolean isPublic;
    
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public String getFacultyUserName() {
        return facultyUserName;
    }

    public void setFacultyUserName(String facultyUserName) {
        this.facultyUserName = facultyUserName;
    }

    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDay() {
        return eventDay;
    }

    public void setEventDay(String eventDay) {
        this.eventDay = eventDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public void setSlotSize(int slotSize) {
        this.slotSize = slotSize;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public String createEvent() throws ParseException, SQLException{
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        
        
         //FORMATER FOR EVENT DAY
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
        Date date = f.parse(this.eventDay);
        java.sql.Date sqlEventDay = new java.sql.Date(date.getTime());
        
        SimpleDateFormat timeF = new SimpleDateFormat("hh:mm a");
        String tempStart = makeTimeString(this.startTime);
        String tempEnd   = makeTimeString(this.endTime);
        
        // MAKE SURE ONE TIME INSNT' AFTER THER OTHER
        Timestamp tsStart = new Timestamp(timeF.parse(tempStart).getTime());
        Timestamp tsEnd = new Timestamp(timeF.parse(tempEnd).getTime());
        
        String sqlIsPublic;
        if(this.isPublic){
            sqlIsPublic = "T";
        }else{
            sqlIsPublic = "F";
        }
        

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
                query = "INSERT INTO EVENTS(FACULTY_USERNAME, EVENT_NAME, DAY, START_TIME, END_TIME, "
                        + "SLOT_SIZE, ACTIVE) VALUES(?, ?, ?, ?, ?, ?, ?)";

                 PreparedStatement stmt = conn.prepareStatement(query,
                             Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, username);
                stmt.setString(2, this.eventName);
                stmt.setDate(3, sqlEventDay);
                stmt.setTimestamp(4, tsStart);
                stmt.setTimestamp(5, tsEnd);
                stmt.setInt(6, slotSize);
                stmt.setString(7, sqlIsPublic);

                stmt.executeUpdate();

                ResultSet gk = stmt.getGeneratedKeys();

                long key;

                if (gk.next()) {
                    key = gk.getLong(1);
                }else{
                    throw new SQLException("CANNOT GET GENERATED KEY");
                }

                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();

                start.setTime(timeF.parse(tempStart));
                end.setTime(timeF.parse(tempEnd));

                long diff = end.getTime().getTime() - start.getTime().getTime();

                long min = (diff/1000)/ 60;        

                long slots = min/this.slotSize;

                System.out.println(slots);

                PreparedStatement slotStmt = conn.prepareStatement("INSERT INTO EVENT_SLOTS(EVENT_ID,START_TIME, STATUS) VALUES(?, ?, ?)");

                for(int i = 0; i < slots; i++){
                    Timestamp ts = new Timestamp(start.getTimeInMillis());
                    slotStmt.setLong(1, key);
                    slotStmt.setTimestamp(2, ts);
                    start.add(Calendar.MINUTE, this.slotSize);
                    
                    
                    slotStmt.setString(3, "O");
                    slotStmt.executeUpdate();
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
         
        return "index";
    }
    
    private String makeTimeString(String s){
        
        String ft = s.substring(0, s.length() -2);
        String st = s.substring(s.length() - 2, s.length());
        
        String out = ft + " " + st.toUpperCase();
        
        return out;
    }
    
}

package event;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import java.util.logging.Level;
import java.util.logging.Logger;


@Named
@SessionScoped
public class StudentCalendarBean implements Serializable{
    
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    private DefaultScheduleModel eventModal;
    private CalendarEvent event = new CalendarEvent();
    private CalendarEvent newEvent;
    private boolean slotView = false;
    private String view = "agendaWeek";
    private int timeScope;
    private int timeStep = 30;
    ArrayList<CalendarEvent> modifiedSlots = new ArrayList<>();
    

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }
    
    public boolean getSlotView(){
        return this.slotView;
    }
    
    public void setSlotView(boolean v){
        this.slotView = v;
    }
    
    public int getTimeScope(){
        if(this.slotView){
            return this.timeScope;
        } else {
            return 10;
        }
    }
    
    public void setTimeScope(int i){
        this.timeScope = i;
    }
        
    @PostConstruct
    public void init() {
        eventModal = new DefaultScheduleModel();
        try {
            this.loadAllEvents();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarEventBean.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }   
    
    public String getView(){
        return this.view;
    }
    
    public void setView(String v){
        this.view = v;
    }
    
    public ScheduleModel getEventModel() {
        return eventModal;
    }
    
    public CalendarEvent getEvent() {        
        return this.event;
    }
        
    public void setEvent(CalendarEvent event) {
        this.event = event;
    }
    
    public void onEventSelect(SelectEvent selectEvent) throws SQLException {

        event = (CalendarEvent) selectEvent.getObject();
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
                String query = "SELECT * FROM EVENTS WHERE EVENT_ID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setInt(1, this.event.getDbID());
                
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next()){
                    

                    this.event.setDbID(rs.getInt("EVENT_ID"));
                    this.event.setStyleClass(rs.getString("STYLE_CLASS"));
                    this.event.setSlotSize(rs.getInt("SLOT_SIZE"));
                    this.event.setStartDate(rs.getTimestamp("START_TIME"));
                    this.event.setEndDate(rs.getTimestamp("END_TIME"));
                    this.event.setDesc(rs.getString("DESCRIPTION"));
                    this.event.setEdit(true);
                }
                
                
                conn.commit();
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }

        }finally{    
            conn.close();
        }
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new CalendarEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        this.event.setEdit(false);
        
    }
     
    public void onViewChange(SelectEvent selectEvent) throws SQLException{
        this.setSlotView(false);
        this.loadAllEvents();
        this.setTimeStep(30);
        String viewName = selectEvent.getObject().toString();
        this.setView(viewName);
    }
     
    private String getUserEmail(){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String email = request.getRemoteUser();
        
        return email;
    }
    
    public void loadAllEvents() throws SQLException{
        
        System.out.println("IN LOAD ALL");
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        this.eventModal.clear();
                        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                String query = "SELECT "
                        + "EVENT_SLOTS.START_TIME, "
                        + "EVENT_SLOTS.END_TIME, "
                        + "EVENT_SLOTS.EVENT_ID, "
                        + "EVENTS.EVENT_NAME, "
                        + "EVENTS.STYLE_CLASS, "
                        + "EVENTS.DESCRIPTION "
                        + "FROM EVENT_SLOTS "
                        + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                        + "WHERE EVENT_SLOTS.USER_INFO_EMAIL = ? AND EVENT_SLOTS.STATUS = 'T'";
                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setString(1, this.getUserEmail());
                
                ResultSet rs = stmt.executeQuery();
                
                System.out.println("USERNAME: " + this.getUserEmail());
                
                while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setDbID(rs.getInt("EVENT_ID"));
                    ca.setTitle(rs.getString("EVENT_NAME"));                    
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    ca.setStyleClass(rs.getString("STYLE_CLASS"));
                    ca.setDesc(rs.getString("DESCRIPTION"));
                    

                              
                    this.eventModal.addEvent(ca);
                }
                
                conn.commit();
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }

        }finally{    
            conn.close();
        }
    }
    
}

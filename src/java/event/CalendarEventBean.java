package event;

import announcements.utility.Utility;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.mail.MessagingException;
import registration.GoogleMail;

@Named
@SessionScoped
public class CalendarEventBean implements Serializable{
    
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    private DefaultScheduleModel eventModal;
    private CalendarEvent event = new CalendarEvent();
    private boolean slotView = false;
    private String view = "agendaWeek";
    private int timeScope;
    private int timeStep = 30;
    ArrayList<CalendarEvent> modifiedSlots = new ArrayList<>();
    private String modifyMethod = "";
    
    // INITALIZE EVENT MODAL AND LOAD ALL THE ACTIVE EVENTS INTO THE MODAL------
    @PostConstruct
    public void init() {
        eventModal = new DefaultScheduleModel();
        try {
            this.loadAllEvents();
        } catch (SQLException ex) {
            System.out.println("DB ERROR COULD NOT GET EVENTS");
        }
  
    }   
    //--------------------------------------------------------------------------
    
    // -------------------------------------------------------------------------
    // TIMESTEP SETS THE NUMBER OF MINUTES SHOW FOR EACH STEP ON THE CALENDARE
    // IN THE AGENDA VIEWS SMALLER VALUES WILL MAKE SHORTER EVENTS SHOW UP
    // LARGER
    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }
    // -------------------------------------------------------------------------
    
    // -------------------------------------------------------------------------
    // TIMESCOPE SETS THE FIRST HOUR THAT THE CALENDAR WILL SHOW IF THE CALENDAR
    // NEEDS TO SCROLL TO SHOW ALL TIMES
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
    // -------------------------------------------------------------------------    

    //--------------------------------------------------------------------------
    // RETURNS THE CURRENT VIEW TO BE FOR THE CALENDAR TO SHOW WEEK/DAY/VIEW
    public String getView(){
        return this.view;
    }
    
    public void setView(String v){
        this.view = v;
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // THE ACTUAL CALENDAR
    public ScheduleModel getEventModel() {
        return eventModal;
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // EVENT IS THE CURRENT INDIVIDUAL EVENT BEING WORKED WITH
    public CalendarEvent getEvent() {        
        return this.event;
    }
    
        
    public void setEvent(CalendarEvent event) {
        this.event = event;
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // ADD THE CURRENT EVENT TO THE MODAL AND DATABASE
    public void addEvent(ActionEvent actionEvent) throws SQLException {
        if(event.getId() == null){              
            eventModal.addEvent(event);
            createEvent();
            this.loadAllEvents();
        }else{
            eventModal.updateEvent(event);
            this.updateEvent();
            event = new CalendarEvent();
        }
    }
    //--------------------------------------------------------------------------
    
    // SET THE CURRENT EVENT TO THE SELECTED ONE DB QUERY MIGHT NOT BE NEEDED
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
    //--------------------------------------------------------------------------
    
    //CREATE NEW EVENT FOR THE TIME SELECTED -----------------------------------
    public void onDateSelect(SelectEvent selectEvent) {
        event = new CalendarEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        this.event.setEdit(false);
    }
    //--------------------------------------------------------------------------
    
    
    // WHEN EVENT IS MOVED ALL SLOTS THAT  WILL NO LONGER BE AVAILABLE ARE FOUND
    // INCLUDING ONE THAT HAVE ALREAD BEEN TACKEN UP. THEN ALL EVENTS THAT WILL 
    // BE CREATED IN THE NEW OPEN SLOTS, NEW AND CANCLED SLOTS ARE ADDED TO THE 
    // MODIFIED SLOTS LIST, TO BE PROCESSED AFTER CONFIRMATION. 
    public void onEventMove(ScheduleEntryMoveEvent event) throws SQLException {
        this.modifyMethod = "UPDATE";
        
        this.modifiedSlots.clear();
        
        int totalMin =  event.getMinuteDelta() + (event.getDayDelta() * 24 * 60);
        
        CalendarEvent e = (CalendarEvent) event.getScheduleEvent();
        
        this.event = e;
        
        Calendar newStart = new GregorianCalendar();
        Calendar oldStart = new GregorianCalendar();
        Calendar newEnd = new GregorianCalendar();
        Calendar oldEnd = new GregorianCalendar();
      
        newStart.setTime(e.getStartDate());
        
        oldStart.setTime(e.getStartDate());
        oldStart.add(Calendar.MINUTE, -totalMin);
        
        newEnd.setTime(e.getEndDate());

        oldEnd.setTime(e.getEndDate());
        oldEnd.add(Calendar.MINUTE, -totalMin);
        
               
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
                
                String slotQuery;
                PreparedStatement stmt;
                if(totalMin >= 0){
                     slotQuery = "SELECT "
                        + "EVENT_SLOTS.EVENT_SLOT_ID, "
                        + "EVENT_SLOTS.USER_INFO_EMAIL, "
                        + "EVENT_SLOTS.STATUS, "
                             + "EVENT_SLOTS.START_TIME, "
                             + "EVENT_SLOTS.END_TIME, "
                             + "EVENTS.EVENT_NAME, "
                             + "USER_INFO.FIRSTNAME, "
                             + "USER_INFO.LASTNAME "
                             + "FROM EVENT_SLOTS "
                             + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                             + "JOIN USER_INFO ON USER_INFO.EMAIL = EVENT_SLOTS.USER_INFO_EMAIL "
                        + "WHERE EVENTS.EVENT_ID = ? "
                        + "AND EVENT_SLOTS.START_TIME <= ? "
                        + "AND EVENT_SLOTS.STATUS <> 'C'";
                        stmt = conn.prepareStatement(slotQuery);
                        stmt.setInt(1, e.getDbID());
                        stmt.setTimestamp(2, new Timestamp(newStart.getTimeInMillis()));
               } else {
                    slotQuery = "SELECT "
                        + "EVENT_SLOTS.EVENT_SLOT_ID, "
                        + "EVENT_SLOTS.USER_INFO_EMAIL, "
                        + "EVENT_SLOTS.STATUS, "
                             + "EVENT_SLOTS.START_TIME, "
                             + "EVENT_SLOTS.END_TIME, "
                             + "EVENTS.EVENT_NAME, "
                             + "USER_INFO.FIRSTNAME, "
                             + "USER_INFO.LASTNAME "
                             + "FROM EVENT_SLOTS "
                             + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                             + "JOIN USER_INFO ON USER_INFO.EMAIL = EVENT_SLOTS.USER_INFO_EMAIL "
                        + "WHERE EVENTS.EVENT_ID = ? "
                        + "AND EVENT_SLOTS.START_TIME >= ? "
                        + "AND EVENT_SLOTS.STATUS <> 'C'";
                        stmt = conn.prepareStatement(slotQuery);
                        stmt.setInt(1, e.getDbID());
                        stmt.setTimestamp(2, new Timestamp(e.getEndDate().getTime()));
                } 

                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setTitle(rs.getString("EVENT_NAME"));
                    ca.setUserEmail(rs.getString("USER_INFO_EMAIL"));
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    ca.setUserFirstName(rs.getString("FIRSTNAME"));
                    ca.setUserLastName(rs.getString("LASTNAME"));
                    ca.setChangeStatus("REMOVE");
                    ca.setDbID(rs.getInt("EVENT_SLOT_ID"));
                    
                    if(rs.getString("STATUS").equals("O")){
                        ca.setUserFirstName("OPEN SLOT");
                        ca.setUserLastName("");
                    }
                    
                                       
                    this.modifiedSlots.add(ca);
                }
                
                if(totalMin > 0){
                    if(newStart.compareTo(oldEnd) < 0){
                        int slots = totalMin/e.getSlotSize();
                        for(int i= 0; i < slots; i++){
                            CalendarEvent c = new CalendarEvent();
                            c.setTitle(e.getTitle());
                            c.setUserFirstName("NEW OPEN SLOT");
                            c.setStartDate(oldEnd.getTime());
                            oldEnd.add(Calendar.MINUTE, e.getSlotSize());
                            c.setEndDate(oldEnd.getTime());
                            c.setChangeStatus("CREATE");
                            this.modifiedSlots.add(c);
                       }
                    } else{
                        long difMin = ((newEnd.getTimeInMillis() - newStart.getTimeInMillis())/1000)/60;
                        long slots = difMin/e.getSlotSize();
                        for(int i= 0; i < slots; i++){
                            CalendarEvent c = new CalendarEvent();
                            c.setTitle(e.getTitle());
                            c.setUserFirstName("NEW OPEN SLOT");
                            c.setStartDate(newStart.getTime());
                            newStart.add(Calendar.MINUTE, e.getSlotSize());
                            c.setEndDate(newStart.getTime());
                            c.setChangeStatus("CREATE");
                            this.modifiedSlots.add(c);  
                        }
                    }
                } else{
                    if(newEnd.compareTo(oldStart) < 0){
                        long difMin = ((newEnd.getTimeInMillis() - newStart.getTimeInMillis())/1000)/60;
                        long slots = difMin/e.getSlotSize();
                        for(int i= 0; i < slots; i++){
                            CalendarEvent c = new CalendarEvent();
                            c.setTitle(e.getTitle());
                            c.setUserFirstName("NEW OPEN SLOT");
                            c.setStartDate(newStart.getTime());
                            newStart.add(Calendar.MINUTE, e.getSlotSize());
                            c.setEndDate(newStart.getTime());
                            c.setChangeStatus("CREATE");
                            this.modifiedSlots.add(c);
                        }
                    } else {
                        int slots = -totalMin/e.getSlotSize();
                        for(int i= 0; i < slots; i++){
                            CalendarEvent c = new CalendarEvent();
                            c.setTitle(e.getTitle());
                            c.setUserFirstName("NEW OPEN SLOT");
                            c.setStartDate(newStart.getTime());
                            newStart.add(Calendar.MINUTE, e.getSlotSize());
                            c.setEndDate(newStart.getTime());
                            c.setChangeStatus("CREATE");
                            this.modifiedSlots.add(c);
                        }
                    }
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
                
        //MAKE SURE NEW START DATE PLUS LENGHT ISN'T IN THE PAST.
        
        
        
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // WHEN EVENT IS REZIED ALL SLOTS THAT  WILL NO LONGER BE AVAILABLE ARE FOUND
    // INCLUDING ONE THAT HAVE ALREAD BEEN TACKEN UP. THEN ALL EVENTS THAT WILL 
    // BE CREATED IN THE NEW OPEN SLOTS, NEW AND CANCLED SLOTS ARE ADDED TO THE 
    // MODIFIED SLOTS LIST, TO BE PROCESSED AFTER CONFIRMATION. 
    public void onEventResize(ScheduleEntryResizeEvent event) throws SQLException {
        this.modifyMethod = "UPDATE";
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
        
        this.modifiedSlots.clear();
        int totalChange = event.getDayDelta() * 24 * 60;
        totalChange += event.getMinuteDelta();
        
        CalendarEvent e = (CalendarEvent)event.getScheduleEvent();
        
        this.setEvent(e);
        

        
        if(totalChange > 0){
            int slots = totalChange/e.getSlotSize();
 
            Calendar newEnd = new GregorianCalendar();
            newEnd.setTime(e.getEndDate());
            newEnd.add(Calendar.MINUTE, -totalChange);
            
            for(int i = 0; i < slots; i++){
                CalendarEvent ca = new CalendarEvent();
                
                ca.setDbID(e.getDbID());
                ca.setStartDate(new Timestamp(newEnd.getTimeInMillis()));
                newEnd.add(Calendar.MINUTE, e.getSlotSize());
                ca.setEndDate(new Timestamp(newEnd.getTimeInMillis()));
                ca.setUserFirstName("NEW OPEN SLOT");
                ca.setTitle(e.getTitle());
                ca.setChangeStatus("CREATE");
                this.modifiedSlots.add(ca);
            }
        } else {

            String slotQuery = "SELECT "
                                + "EVENT_SLOTS.STATUS, "
                                + "EVENT_SLOTS.EVENT_SLOT_ID, "
                                + "EVENT_SLOTS.USER_INFO_EMAIL, "
                                + "EVENT_SLOTS.START_TIME, "
                                + "EVENT_SLOTS.END_TIME, "
                                + "EVENTS.EVENT_NAME, "
                                + "USER_INFO.FIRSTNAME, "
                                + "USER_INFO.LASTNAME "
                                + "FROM EVENT_SLOTS "
                                + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                                + "JOIN USER_INFO ON USER_INFO.EMAIL = EVENT_SLOTS.USER_INFO_EMAIL "
                            + "WHERE EVENTS.EVENT_ID = ? "
                            + "AND EVENT_SLOTS.STATUS <> 'C'"
                            + "AND EVENT_SLOTS.START_TIME >= ?";
            
            PreparedStatement stmt = conn.prepareStatement(slotQuery);
            stmt.setInt(1, e.getDbID());
            stmt.setTimestamp(2, new Timestamp(e.getEndDate().getTime()));
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setTitle(rs.getString("EVENT_NAME"));
                    ca.setUserEmail(rs.getString("USER_INFO_EMAIL"));
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    
                    ca.setUserFirstName(rs.getString("FIRSTNAME"));
                    ca.setUserLastName(rs.getString("LASTNAME"));
                    ca.setChangeStatus("REMOVE");
                    ca.setDbID(rs.getInt("EVENT_SLOT_ID"));
                    
                    if(rs.getString("STATUS").equals("O")){
                        ca.setUserFirstName("OPEN SLOT");
                        ca.setUserLastName("");
                    }
                                       
                    this.modifiedSlots.add(ca);
                }
        }
        
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // IF SWITCHING FROM FROM AGENDAVIEW(SLOTS) TO ANOTHER VIEW RELEAD THE EVENTS
    // RESET THE TIMESTEP TO THIRTY
    public void onViewChange(SelectEvent selectEvent) throws SQLException{
        
        String viewName = selectEvent.getObject().toString();
        
        if(this.getView().equals("agendaDay") && !viewName.equals("agendaDay")){
            this.loadAllEvents();
            this.setTimeStep(30);
        }
     
        this.setView(viewName);
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // ADD A NEW MESSAGE
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    //--------------------------------------------------------------------------
    
    // -------------------------------------------------------------------------
    // CREATE EVENTS CREATES A NEW EVENT TO CALCULATES EACH OF THE NEW SLOTS AND 
    // INSERTS THE NEW SLOTS INTO THE DATABASE
    private void createEvent() throws SQLException{
        
        if(this.event.getSlotSize() <= 0){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "NO ZERO!", null);
            addMessage(message);
        }
        
        if(FacesContext.getCurrentInstance().getMessageList().size() > 0){
            return; 
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
                query = "INSERT INTO EVENTS(FACULTY_EMAIL, FACULTY_DISPLAY_NAME, EVENT_NAME,START_TIME, END_TIME, "
                        + "SLOT_SIZE, ACTIVE, STYLE_CLASS, DESCRIPTION) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement stmt = conn.prepareStatement(query,
                                               Statement.RETURN_GENERATED_KEYS);
                 
                stmt.setString(1, this.getUserEmail());
                stmt.setString(2, this.getDisplayName());
                stmt.setString(3, this.event.getTitle());
                stmt.setTimestamp(4, new Timestamp(this.event.getStartDate().getTime()));
                stmt.setTimestamp(5, new Timestamp(this.event.getEndDate().getTime()));
                stmt.setInt(6, this.event.getSlotSize());
                stmt.setString(7, "T");
                stmt.setString(8, this.event.getStyleClass());
                stmt.setString(9, this.event.getDesc());
                //stmt.setString(9, this.event.getDescription());
                 
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
        
                start.setTime(this.event.getStartDate());
                end.setTime(this.event.getEndDate());
        
                long diff = end.getTime().getTime() - start.getTime().getTime();
                long min = (diff/1000)/ 60;
                long slots = min/this.event.getSlotSize();
                
                String slotQuery = "INSERT INTO EVENT_SLOTS(EVENT_ID, USER_INFO_EMAIL, USER_INFO_USERNAME, START_TIME, END_TIME, STATUS) "
                                    + "VALUES(?, ?, ?, ?, ?, ?)";
                 
                PreparedStatement slotStmt = conn.prepareStatement(slotQuery);
                
                for(int i = 0; i < slots; i++){
                    slotStmt.setLong(1, key);
                    slotStmt.setString(2, this.getUserEmail());
                    slotStmt.setString(3, "OPEN SLOT");
                    slotStmt.setTimestamp(4, new Timestamp(start.getTimeInMillis()));
                    start.add(Calendar.MINUTE, this.event.getSlotSize());
                    slotStmt.setTimestamp(5, new Timestamp(start.getTimeInMillis()));
                    slotStmt.setString(6, "O");
                    
                    slotStmt.executeUpdate();
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
    
    private String getUserEmail(){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String email = request.getRemoteUser();
        
        return email;
    }
    
    //--------------------------------------------------------------------------
    // SET THE NAME TO DISPLAY WHEN SHOWING WHICH INSTRUCTOR OWNS THE EVENT
    private String getDisplayName() throws SQLException{
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String email = request.getRemoteUser();
        
        String displayName = "";
        
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
               String query = "SELECT TITLE, LASTNAME FROM USER_INFO WHERE EMAIL = ?";

                 PreparedStatement stmt = conn.prepareStatement(query);
                 
                 stmt.setString(1, this.getUserEmail());

                ResultSet rs = stmt.executeQuery();
                
                if(rs.next()){
                    displayName += rs.getString("TITLE");
                    displayName += " " + rs.getString("LASTNAME");
                } else{
                    throw new SQLException("CANNOT FIND USER");
                }
                 
                 
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }
        }finally{    
            conn.close();
        } 
        return displayName;
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // SETS THE VIEW TO ONLY SHOW DAY SETS VIEW VARIABLES BASED ON WHAT
    // EVENTS ARE SHOWN
    public void viewSlots() throws SQLException{
        this.setView("agendaDay");
        
        this.loadSlots();
        
        Long diff = this.event.getEndDate().getTime() - this.event.getStartDate().getTime();
        Long seconds = diff/1000;
        Long minutes = seconds/60;
        double hours = minutes/60.00;
        int intHours = (int)Math.ceil(hours);
        
        this.setTimeStep( (int) (this.event.getSlotSize() * .5) );
        
        Calendar c = new GregorianCalendar();
        c.setTime(this.event.getStartDate());
        this.setTimeScope(c.get(Calendar.HOUR_OF_DAY));
        
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // LOAD ALL THE EVENTS FOR THE CURRENT INSTRUCTOR INTO THE MODAL
    public void loadAllEvents() throws SQLException{
        
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
                String query = "SELECT * FROM EVENTS WHERE FACULTY_EMAIL = ? AND ACTIVE = 'T'";
                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setString(1, this.getUserEmail());
                
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setDbID(rs.getInt("EVENT_ID"));
                    ca.setTitle(rs.getString("EVENT_NAME"));                    
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    ca.setStyleClass(rs.getString("STYLE_CLASS"));
                    ca.setDesc(rs.getString("DESCRIPTION"));
                    ca.setSlotSize(rs.getInt("SLOT_SIZE"));
                              
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
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // WHEN VIEWING VIEW SLOTS IS SELECTED CLEAR THE MODAL AND LOAD THE SLOTS
    // FOR THE CURRENT EVENT
    public void loadSlots() throws SQLException{
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
                String query = "SELECT EVENT_SLOTS.USER_INFO_USERNAME, EVENT_SLOTS.START_TIME, EVENT_SLOTS.END_TIME, EVENTS.STYLE_CLASS FROM EVENT_SLOTS "
                                + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                                + "WHERE EVENTS.EVENT_ID = ? "
                                + "AND EVENT_SLOTS.STATUS <> 'C'";

                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setInt(1, getEvent().getDbID());
                
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setTitle(rs.getString("USER_INFO_USERNAME"));
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    ca.setStyleClass(rs.getString("STYLE_CLASS"));
                              
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
    //--------------------------------------------------------------------------
    
    // -------------------------------------------------------------------------
    // COMMIT ALL THE SLOTS IN THE MODIFIED SLOTS LIST
    public void modifyEvent() throws SQLException, MessagingException{
        
        
        if (ds == null) {
            throw new SQLException("CANNOT GET DATASOURCE");
        }

        Connection conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("CANNOT GET DATABASE");
        }
                
        GoogleMail m = new GoogleMail();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        try {     
            boolean committed = false;
            conn.setAutoCommit(false);
            try{
                                
                for(CalendarEvent ca:this.modifiedSlots){
                    if(ca.getChangeStatus().equals("REMOVE")){
                    
                        String title = "UCO: Appointment Reminder";     
                        String emailMessage = "<p>Attention</p> "
                                + "<h3>Your Appointement Has Been Caceled.</h3>"
                                + "Event Name:" + ca.getTitle() + "<br/>"
                                + "Event Time:" + ca.getStartTime() +"<br/>";

                        GoogleMail.Send(externalContext,
                                "barnettlynn@gmail.com",
                                title,
                                emailMessage);

                        String cQuery = "UPDATE EVENT_SLOTS SET STATUS = 'C' WHERE EVENT_SLOT_ID = ?";
                        
                        
                        PreparedStatement cStmt = conn.prepareStatement(cQuery);
                        cStmt.setInt(1, ca.getDbID());
                        cStmt.executeUpdate();
                        
                    } else if(ca.getChangeStatus().equals("CREATE")) {
                        String slotQuery = "INSERT INTO EVENT_SLOTS(EVENT_ID, USER_INFO_EMAIL, USER_INFO_USERNAME, START_TIME, END_TIME, STATUS) "
                                    + "VALUES(?, ?, ?, ?, ?, ?)";
                        PreparedStatement slotStmt = conn.prepareStatement(slotQuery);
                        slotStmt.setLong(1, this.event.getDbID());
                        slotStmt.setString(2, this.getUserEmail());
                        slotStmt.setString(3, "OPEN SLOT");
                        slotStmt.setTimestamp(4, new Timestamp(ca.getStartDate().getTime()));
                        slotStmt.setTimestamp(5, new Timestamp(ca.getEndDate().getTime()));
                        slotStmt.setString(6, "O");
                        slotStmt.executeUpdate();
                    }

                }
                
                if(this.modifyMethod.equals("UPDATE")){
                    System.out.println("SEETING START TO: " + this.event.getStartDate());
                    System.out.println("SETTING EDN TO: " + this.getEvent().getEndDate());
                    String eQuery = "UPDATE EVENTS SET START_TIME = ?, END_TIME = ? WHERE EVENT_ID = ?";
                    PreparedStatement eStmt = conn.prepareStatement(eQuery);
                    eStmt.setTimestamp(1, new Timestamp(this.event.getStartDate().getTime()));
                    eStmt.setTimestamp(2, new Timestamp(this.event.getEndDate().getTime()));
                    eStmt.setInt(3, this.event.getDbID());
                    eStmt.executeUpdate();
                } else if(this.modifyMethod.equals("DELETE")){
                    String eQuery = "UPDATE EVENTS SET ACTIVE = 'C' WHERE EVENT_ID = ?";
                    PreparedStatement eStmt = conn.prepareStatement(eQuery);
                    eStmt.setInt(1, this.event.getDbID());
                    eStmt.executeUpdate();
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
        
        
        this.modifyMethod = "";
        this.loadAllEvents();
    }
    //--------------------------------------------------------------------------
    
    // -------------------------------------------------------------------------
    // LOAD ALL THE SLOTS THAT WILL BE CANCLED FOR THE CURRENT EVENT 
    public void onCancelEvent() throws SQLException{
        this.modifyMethod = "DELETE";
        
        this.modifiedSlots.clear();
        
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
                
                String slotQuery = "SELECT EVENT_SLOTS.EVENT_SLOT_ID, "
                                    + "EVENT_SLOTS.USER_INFO_USERNAME, "
                                    + "EVENT_SLOTS.USER_INFO_EMAIL, "
                                    + "EVENT_SLOTS.START_TIME, "
                                    + "EVENT_SLOTS.END_TIME, "
                                    + "EVENT_SLOTS.STATUS, "
                                    + "EVENTS.EVENT_NAME, "
                                    + "USER_INFO.FIRSTNAME, "
                                    + "USER_INFO.LASTNAME "
                            + "FROM EVENT_SLOTS "
                            + "JOIN EVENTS ON EVENTS.EVENT_ID = EVENT_SLOTS.EVENT_ID "
                            + "JOIN USER_INFO ON USER_INFO.EMAIL = EVENT_SLOTS.USER_INFO_EMAIL "
                            + "WHERE EVENTS.EVENT_ID = ? "
                            + "AND STATUS <> 'C'";

                PreparedStatement stmt = conn.prepareStatement(slotQuery);
                
                stmt.setInt(1, this.event.getDbID());
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    CalendarEvent ca = new CalendarEvent();
                    ca.setDbID(rs.getInt("EVENT_SLOT_ID"));
                    ca.setTitle(rs.getString("EVENT_NAME"));
                    ca.setUserFirstName("FIRSTNAME");
                    ca.setUserLastName("LASTNAME");
                    ca.setStartDate(rs.getTimestamp("START_TIME"));
                    ca.setEndDate(rs.getTimestamp("END_TIME"));
                    ca.setChangeStatus("REMOVE");
                    
                    if(rs.getString("STATUS").equals("O")){
                        ca.setUserFirstName("OPEN SLOT");
                        ca.setUserLastName("");
                    }
                    
                   this.modifiedSlots.add(ca);
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
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // UPDATE THE ATRIBUTES FOR THE CURRENT EVENT
    public void updateEvent() throws SQLException{        
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
                String query = "UPDATE EVENTS SET EVENT_NAME = ?, START_TIME = ?, END_TIME = ?, SLOT_SIZE= ?, STYLE_CLASS=?, DESCRIPTION=? WHERE EVENT_ID = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setString(1, this.event.getTitle());
                stmt.setTimestamp(2, new Timestamp(this.event.getStartDate().getTime()));
                stmt.setTimestamp(3, new Timestamp(this.event.getEndDate().getTime()));
                stmt.setInt(4, this.event.getSlotSize());
                stmt.setString(5, this.event.getStyleClass());
                stmt.setString(6, this.event.getDesc());
                stmt.setInt(7, this.event.getDbID());
                stmt.executeUpdate();
                
                
                
                conn.commit();
            } finally {
                if (!committed) {
                    conn.rollback();
                }
            }

        }finally{    
            conn.close();
        }
        this.loadAllEvents();
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // GET ALL THE SLOTS THAT WILL BE CHANGED
    public ArrayList<CalendarEvent> getModifiedSlots() {
        return modifiedSlots;
    }
    //--------------------------------------------------------------------------
}

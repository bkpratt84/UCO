package event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.primefaces.model.DefaultScheduleEvent;

public class CalendarEvent extends DefaultScheduleEvent {
    private int slotSize;
    private boolean active;
    private int dbID;
    private boolean edit;
    private String desc;
    private String ChangeStatus;
    
    //USER A USER CLASS;
    
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    
    
    public boolean getEdit(){
        return this.edit;
    }
    
    public void setEdit(boolean e){
        this.edit = e;
    }
    
    public CalendarEvent(){}
    
    public CalendarEvent(String title, Date startDate, Date endDate){
        super(title, startDate, endDate);
    }
    
    public void setStartTime(String st) throws ParseException{
        SimpleDateFormat ts = new SimpleDateFormat("EEE MMM dd hh:mm a z yyyy");
        SimpleDateFormat sts = new SimpleDateFormat("EEE MMM dd");
        SimpleDateFormat stst = new SimpleDateFormat("z yyyy");

        super.setStartDate(ts.parse(sts.format(this.getStartDate()) + " " + st + " " + stst.format(this.getStartDate()))); 
    }
    
    public String getStartTime(){
        
        if(this.getStartDate() == null){
            return "";
        }
        SimpleDateFormat ht = new SimpleDateFormat("hh:mm a"); 
        return ht.format(this.getStartDate());
    }
    
    public String getStartString(){
        if(this.getStartDate() == null){
            return "";
        }
        SimpleDateFormat ht = new SimpleDateFormat("MM/dd/yyyy hh:mm a"); 
        return ht.format(this.getStartDate());
    }
    
    public void setEndTime(String et) throws ParseException{
        SimpleDateFormat ts = new SimpleDateFormat("EEE MMM dd hh:mm a z yyyy");
        SimpleDateFormat sts = new SimpleDateFormat("EEE MMM dd");
        SimpleDateFormat stst = new SimpleDateFormat("z yyyy");

        super.setEndDate(ts.parse(sts.format(this.getEndDate()) + " " + et + " " + stst.format(this.getEndDate())));
    }
    
    public String getEndTime(){
        
        if(this.getEndDate() == null){
            return "";
        }
        SimpleDateFormat ht = new SimpleDateFormat("hh:mm a");
        return ht.format(this.getEndDate());
    }
    
    public int getSlotSize(){
        return this.slotSize;
    }
    
    public void setSlotSize(int sz){
        this.slotSize = sz;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }


    public String getDesc() {
        return this.desc;
    }


    public void setDesc(String d) {
        this.desc = d;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getChangeStatus() {
        return this.ChangeStatus;
    }

    public void setChangeStatus(String status) {
        this.ChangeStatus = status;
    }
    
    
}

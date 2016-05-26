

package event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class EventHolder {
    private int     eventId;
    private int     facultyId;
    private String  eventName;
    private String  day;
    private String  startTime;
    private String  endTime;
    private int     slotSize;
    private String  active;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
    public boolean alreadyOccured() throws ParseException{
        
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal  = Calendar.getInstance();
        cal.setTime(df.parse(this.day));
    
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
    
        return cal.before(today);
    }
}


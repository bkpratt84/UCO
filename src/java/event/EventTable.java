package event;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Named
@SessionScoped

public class EventTable implements Serializable {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;

    public CalendarEvent getNextEvent() throws SQLException {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();
        CalendarEvent event = new CalendarEvent();

        Connection conn = ds.getConnection();
        try {
            SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
            Calendar cal = Calendar.getInstance();
            dayFormat.format(cal.getTime());

            String query = "SELECT * FROM EVENTS "
                        + "WHERE FACULTY_EMAIL = ? "
                        + "AND ACTIVE = 'T' "
                        + "AND START_TIME >= ? "
                        + "ORDER BY START_TIME "
                        + "FETCH NEXT 1 ROWS ONLY";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            Calendar c = new GregorianCalendar();

            stmt.setString(1, username);
            stmt.setTimestamp(2, new Timestamp(c.getTimeInMillis()));

            ResultSet eventsResults = stmt.executeQuery();

            while (eventsResults.next()) {
                
                event.setTitle(eventsResults.getString("EVENT_NAME"));
                event.setDbID(eventsResults.getInt("EVENT_ID"));
                event.setStartDate(eventsResults.getTimestamp("START_TIME"));

            }

        } finally {
            conn.close();
        }

        return event;
    }

    public ArrayList<EventHolder> getEvents() throws SQLException {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String username = request.getRemoteUser();

        ArrayList<EventHolder> events = new ArrayList<>();

        Connection conn = ds.getConnection();
        try {

            String query = "SELECT * FROM EVENTS WHERE FACULTY_USERNAME = ? AND ACTIVE='T'";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, username);

            ResultSet eventsResults = stmt.executeQuery();

            SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");

            while (eventsResults.next()) {
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

}

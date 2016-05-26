package event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
@RequestScoped
public class slotMgmt {
    private int eventId;
    private ArrayList<Slots> slots;
    
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    public void setEventId(int id){
        this.eventId = id;
    }
    
    public int getEventId(){
        return this.eventId;
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
                String query = "SELECT * FROM EVENT_SLOTS WHERE EVENT_ID = ? AND STATUS = 'O'";

                PreparedStatement stmt = conn.prepareStatement(query);
                 
                stmt.setInt(1, this.eventId);
                 
                ResultSet rs = stmt.executeQuery();
               
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                
                while(rs.next()){
                    Slots s = new Slots();
                    
                    Timestamp st = rs.getTimestamp("START_TIME");
                    s.setStartTime(timeFormat.format(st));

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
        
        return this.slots;
    }
    
    
}

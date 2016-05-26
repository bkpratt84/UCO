
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
public class SlotFacultyView {
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    
    public ArrayList<Slots> getSlots() throws SQLException{
        
        ArrayList<Slots> slots = new ArrayList<>();
        
        Connection conn = ds.getConnection();
        try{
            
            String query = "SELECT * FROM EVENT_SLOTS";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            ResultSet slotResults = stmt.executeQuery();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
            
            while(slotResults.next()){

                Slots s = new Slots();
                    
                Timestamp tempTS = slotResults.getTimestamp("START_TIME");
                
                s.setStartTime(timeFormat.format(tempTS));
                
                slots.add(s);
                
            }
            
            
            
        } finally {
            conn.close();
        }
        
        return slots;
    }
}

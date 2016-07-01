package announcements.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;

@Named(value = "postService")
@RequestScoped
public class PostService {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;

    public int commentCount(int parentID) throws SQLException {
        int count = 0;
        
        if (ds == null) {
            throw new SQLException("ds is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("conn is null; Can't get db connection");
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM post WHERE parentId = ?"
            );
            
            ps.setInt(1, parentID);
            ResultSet result = ps.executeQuery();
            
            result.next();
            count = result.getInt(1);
        } finally {
            conn.close();
        }  
        
        return count;
    }
}
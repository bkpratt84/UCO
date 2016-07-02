package announcements.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;

@Named(value = "UserService")
@RequestScoped
public class UserService {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    
    public boolean emailExists(String email) throws SQLException {
        int count = -1;
        
        if (ds == null) {
            throw new SQLException("ds is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("conn is null; Can't get db connection");
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM user_info WHERE email = ?"
            );

            ps.setString(1, email);
            ResultSet result = ps.executeQuery();
            
            result.next();
            count = result.getInt(1);
        } finally {
            conn.close();
        }  
        
        return (count != 0);
    }
    
    public String getUserName(int ID) throws SQLException {
        String username = null;
        
        if (ds == null) {
            throw new SQLException("ds is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("conn is null; Can't get db connection");
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT username FROM user_info WHERE id = ?"
            );
            
            ps.setInt(1, ID);
            ResultSet result = ps.executeQuery();
            
            result.next();
            
            username = result.getString("username");
        } finally {
            conn.close();
        }  
        
        return username;
    }
    
    public int getUserId(String userName) throws SQLException {
        int userId;
        
        if (ds == null) {
            throw new SQLException("ds is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("conn is null; Can't get db connection");
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM user_info WHERE username= ?"
            );
            
            ps.setString(1, userName);
            ResultSet result = ps.executeQuery();
            
            result.next();
            
            userId = result.getInt("id");
        } finally {
            conn.close();
        }  
        
        return userId;
    }
}
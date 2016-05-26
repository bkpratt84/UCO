package bookPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
@ApplicationScoped
public class BookFunctions {
    @Resource(name="jdbc/cs_db_Datasource")
    private DataSource ds;
    
    private String query;
    private String searchType = "CRN";
    private Boolean myClasses = false;
    
    //add a book to the database
    public void addBook(String bookCrn) throws SQLException{
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        int result;
        try{
            stmt.executeUpdate("insert into books (crn, title, edition, isbn, required) values ('"+bookCrn+"', 'update title', 'update edition', 'update isbn', 'false')");           
        }finally{conn.close();}
    }
    
    //get a list of books for a given class based on the crn
    public ArrayList<Book> bookInfo(String bookCrn) throws SQLException{
        ArrayList<Book> myBookInfo = new ArrayList<Book>();
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        try{
            ResultSet result = stmt.executeQuery("select * from books where crn = '"+bookCrn+"'");
            while(result.next()){
                myBookInfo.add(new Book(result.getInt("id"), result.getString("crn"), result.getString("title"), result.getString("edition"), result.getString("isbn"), result.getBoolean("required")));
            }
        }finally{conn.close();}
        return myBookInfo;
    }
    
    //get a list of classes to sort the book information
    public ArrayList<BookClass> classInfo() throws SQLException{
        ArrayList<BookClass> myClassInfo = new ArrayList<BookClass>();
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        try{
            String search = "select * from classes";
            if(query != null && !query.trim().equals("")){search = search + " where upper("+searchType+") like upper('%"+query.replaceAll("'", "''")+"%')";}
            search += " order by crn";
            ResultSet result = stmt.executeQuery(search);
            while(result.next()){
                myClassInfo.add(new BookClass(result.getString("crn"), result.getString("subject"), result.getString("course"), result.getString("title"), result.getString("days"), result.getString("times"), result.getInt("capacity"), result.getString("instructor")));
            }
            
            if(myClasses){
                String currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
                result = stmt.executeQuery("select classroll.crn from classroll, user_info where user_info.username = '"+currentUser+"' and user_info.id = classroll.userid");
                ArrayList<String> myClassCrn = new ArrayList<String>();
                while(result.next()){
                    myClassCrn.add(new String(result.getString("crn")));
                }
                boolean found;
                for(int i=0; i<myClassInfo.size(); i++){
                    found = false;
                    BookClass b;
                    for(String s : myClassCrn){
                        b = myClassInfo.get(i);
                        if(b.getCrn().equals(s))
                            found = true;
                    }
                    if(!found){
                        myClassInfo.remove(i);
                        i--;
                    }
                }
            }
        }finally{conn.close();}
        return myClassInfo;
    }
    
    //change whether a book is required or not
    public void changeRequirement(Boolean oldRequirement, int bookId) throws SQLException{
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        int result;
        try{
            if(!oldRequirement){
                result = stmt.executeUpdate("update books set required = 'true' where id = "+bookId);
            }
            else{
                result = stmt.executeUpdate("update books set required = 'false' where id = "+bookId);
            }
            conn.commit();
        }finally{}
    }  
    
    //delete book from the database
    public void deleteBook(int bookId) throws SQLException{
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
       try{
           int reslut = stmt.executeUpdate("delete from books where id ="+bookId);
       }finally{conn.close();}
    }
        
    //clear the search query
    public void clearSearch(){
        query = "";
    }
    
    //update a book in the database
    public void updateBook(String newTitle, String newEdition, String newISBN, String bookCrn, int bookId) throws SQLException{
        Connection conn = ds.getConnection();
            try{
            Statement stmt = conn.createStatement();
            int result;
            String update = "update books set ";
            if(!newTitle.trim().equals("")){update += "title = '"+newTitle.replaceAll("'", "''")+"', ";}
            if(!newEdition.trim().equals("")){update += "edition = '"+newEdition.replaceAll("'", "''")+"', ";}
            if(!newISBN.trim().equals("")){update += "isbn = '"+newISBN.replaceAll("'", "''")+"', ";}
            update += "crn = '"+bookCrn+"' where id = "+bookId;
            result = stmt.executeUpdate(update);
            }finally{conn.close();}       
    }
        
    public void setQuery(String query){
        this.query = query;
    }
    public String getQuery(){
        return query;
    }
    public void setSearchType(String searchType){
        this.searchType = searchType;
    }
    public String getSearchType(){
        return searchType;
    }
    public void setMyClasses(boolean myClasses){
        this.myClasses = myClasses;
    }
    public boolean getMyClasses(){
        return myClasses;
    }
}

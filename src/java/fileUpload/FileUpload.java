package fileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
public class FileUpload implements Serializable {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;

    private String query;
    private String searchType = "FILE_NAME";
    
    private String listOption = "MyFiles";

    private Part file;
    private String fileContent;
    private List<File> list;
    ArrayList<File> fileList = new ArrayList<File>();

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<File> getList() throws SQLException {
        list = listSystemFiles();
        return list;
    }

    public ArrayList<File> listSystemFiles() throws SQLException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externContext = facesContext.getExternalContext();
        String userLoggedIn = externContext.getRemoteUser();

        //String query;
        PreparedStatement checkQuery;

        Connection conn = ds.getConnection();

        try {
            if (externContext.isUserInRole("instructor") && this.listOption.equals("MyFiles")) {
                String search = "Select * from FILES WHERE OWNER_USERNAME = ?";
                if (query != null && !query.trim().equals("")) {
                    search = "Select * from FILES where OWNER_USERNAME = ? AND upper(" + searchType + ") like upper('%" + query + "%')";
                }
                search += " order by ID";
                checkQuery = conn.prepareStatement(search);
                checkQuery.setString(1, userLoggedIn);
            } else {
                String search = "select * from FILES";
                if (query != null && !query.trim().equals("")) {
                    search += " where upper(" + searchType + ") like upper('%" + query + "%')";
                }
                search += " order by ID";

                checkQuery = conn.prepareStatement(search);
            }

            ResultSet result = checkQuery.executeQuery();
            fileList = new ArrayList<File>();
            while (result.next()) {
                int id = result.getInt("ID");
                String fileName = result.getString("FILE_NAME");
                String fileType = result.getString("FILE_TYPE");
                long fileSize = result.getLong("FILE_SIZE");
                Blob fileContent = result.getBlob("FILE_CONTENTS");

                File new_file = new File(id, fileName, fileType, fileSize, fileContent);

                fileList.add(new_file);

            }
            return fileList;
        } finally {
            conn.close();
        }
    }
    
    
        public void uploadFile(FileUploadEvent fileEvent) throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externContext = facesContext.getExternalContext();
        String ownerUsername = externContext.getRemoteUser();

        Connection conn = ds.getConnection();
        
        try {

            PreparedStatement uploadFileQuery = conn.prepareStatement("INSERT INTO FILES (OWNER_USERNAME, FILE_NAME, FILE_TYPE, FILE_SIZE, FILE_CONTENTS) VALUES (?, ?, ?, ?, ?)");
            uploadFileQuery.setString(1, ownerUsername);
            uploadFileQuery.setString(2, fileEvent.getFile().getFileName());
            uploadFileQuery.setString(3, fileEvent.getFile().getContentType());
            uploadFileQuery.setLong(4, fileEvent.getFile().getSize());
            uploadFileQuery.setBinaryStream(5, fileEvent.getFile().getInputstream());
            uploadFileQuery.executeUpdate();
            conn.commit();

        } catch (IOException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage();
            message.setSummary("Something went wrong!");
            message.setDetail("Something went wrong!");
            context.addMessage("mform:file", message);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        message.setSummary("Upload Complete!");
        message.setDetail("UploadComplete!");
        context.addMessage("mform:file", message);

    }
    
    public String downloadFile(int fileID) throws SQLException, IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext context = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        ServletOutputStream outStream = response.getOutputStream();

        Connection conn = ds.getConnection();
        PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM FILES WHERE ID=" + fileID);

        ResultSet result = selectQuery.executeQuery();
        if (!result.next()) {
            facesContext.addMessage("downloadForm:download",
                    new FacesMessage("file download failed for id = " + fileID));
        }

        String fileType = result.getString("FILE_TYPE");
        String fileName = result.getString("FILE_NAME");
        long fileSize = result.getLong("FILE_SIZE");
        Blob fileBlob = result.getBlob("FILE_CONTENTS");

        response.setContentType(fileType);
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + fileName + "\"");

        final int BYTES = 1024;
        int length = 0;
        InputStream in = fileBlob.getBinaryStream();
        byte[] bbuf = new byte[BYTES];

        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            outStream.write(bbuf, 0, length);
        }

        outStream.close();
        conn.close();

        return null;
    }

    public void deleteFile(Integer id) throws SQLException {

        Connection conn = ds.getConnection();
        try {
            PreparedStatement deleteFile = conn.prepareStatement("DELETE FROM FILES where ID=?");
            deleteFile.setInt(1, id);
            deleteFile.executeUpdate();
            conn.commit();

        } finally {
            conn.close();
        }
    }

    //clear the search query
    public void clearSearch() {
        query = "";
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchType() {
        return searchType;
    }
    
    public String getListOption() {
        return listOption;
    }

    public void setListOption(String listOption) {
        this.listOption = listOption;
    }
    
}

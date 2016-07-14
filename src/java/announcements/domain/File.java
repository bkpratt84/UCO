package announcements.domain;

import announcements.utility.Utility;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author foxra
 */
@Entity
@Table(name = "FILES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "File.findAll", query = "SELECT f FROM File f"),
    @NamedQuery(name = "File.findByPostID", query = "SELECT f FROM File f WHERE f.postID = :postID"),
    @NamedQuery(name = "File.findByFileID", query = "SELECT f FROM File f WHERE f.id = :fileID")})
public class File implements Serializable {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    
    @Column(name = "postID")
    private int postID;
    
    @Column(name = "OWNER_ID")
    private int ownerID;
    
    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "FILE_TYPE")
    private String fileType;
    
    @Column(name = "FILE_SIZE")
    private long fileSize;
    
    @Lob
    @Column(name = "FILE_CONTENTS")
    private byte[] fileContent;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "postId")
    private Post post;

    public File() {
        
    }
    
    public File(String fileName, String fileType, long fileSize, byte[] fileContent) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
    
    public String displayFileSize() {
        return Utility.convertBytes(fileSize, true);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
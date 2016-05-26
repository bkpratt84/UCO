package fileUpload;

import java.sql.Blob;

public class File {

    private int id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private Blob fileContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Blob getFileContent() {
        return fileContent;
    }

    public void setFileContent(Blob fileContent) {
        this.fileContent = fileContent;
    }

    public File(int id, String fileName, String fileType, long fileSize, Blob fileContent) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
    }

}

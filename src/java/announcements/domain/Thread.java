package announcements.domain;

import java.time.LocalDate;

public class Thread {
    private int threadID;
    private int author;
    private String title;
    private String message;
    private LocalDate saved;
    private LocalDate lastModified;
    private int lastModifiedBy;
    private String category;

    public Thread(int threadID, int author, String title, String message, LocalDate saved, LocalDate lastModified, int lastModifiedBy) {
        this.threadID = threadID;
        this.author = author;
        this.title = title;
        this.message = message;
        this.saved = saved;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
    }

    public Thread(int threadID, int author, String title, String message, LocalDate saved, LocalDate lastModified, int lastModifiedBy, String category) {
        this.threadID = threadID;
        this.author = author;
        this.title = title;
        this.message = message;
        this.saved = saved;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
        this.category = category;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getSaved() {
        return saved;
    }

    public void setSaved(LocalDate saved) {
        this.saved = saved;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
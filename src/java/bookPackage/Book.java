package bookPackage;


public class Book {
    
    public Book(int id, String crn, String title, String edition, String isbn, boolean required){
        this.id = id;
        this.crn = crn;
        this.title = title;
        this.edition = edition;
        this.isbn = isbn;
        this.required = required;
    }
    private int id;
    private String crn;
    private String title;
    private String edition;
    private String isbn;
    private boolean required;
    
    public void setCrn(String crn){
        this.crn = crn;
    }
    public String getCrn(){
        return crn;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setEdition(String edition){
        this.edition = edition;
    }
    public String getEdition(){
        return edition;
    }
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    public String getIsbn(){
        return isbn;
    }
    public void setRequired(boolean required){
        this.required = required;
    }
    public boolean getRequired(){
        return required;
    }
    public int getId(){
        return id;
    }
}

package library;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private String branch;

    public Book(String title, String author, String isbn, int publicationYear) {
        this(title, author, isbn, publicationYear, "Main");
    }

    public Book(String title, String author, String isbn, int publicationYear, String branch) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.branch = branch;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    @Override
    public String toString() {
        return String.format("%s by %s (ISBN: %s, %d, library.Branch: %s)", title, author, isbn, publicationYear, branch);
    }
}

package library;

public class BookFactory {
    public static Book createBook(String title, String author, String isbn, int year) {
        return new Book(title, author, isbn, year);
    }
    public static Book createBook(String title, String author, String isbn, int year, String branch) {
        return new Book(title, author, isbn, year, branch);
    }
}


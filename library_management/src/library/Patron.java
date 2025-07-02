package library;

import java.util.ArrayList;
import java.util.List;

public class Patron {
    private String name;
    private String id;
    private List<String> borrowedBookIsbns = new ArrayList<>();
    private List<String> borrowingHistory = new ArrayList<>();

    public Patron(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public List<String> getBorrowedBookIsbns() {
        return borrowedBookIsbns;
    }
    public List<String> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void borrowBook(String isbn) {
        borrowedBookIsbns.add(isbn);
        borrowingHistory.add(isbn);
    }
    public void returnBook(String isbn) {
        borrowedBookIsbns.remove(isbn);
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) | Borrowed: %s", name, id, borrowedBookIsbns);
    }
}

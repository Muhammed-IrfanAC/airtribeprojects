package library;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private String name;
    private List<String> bookIsbns = new ArrayList<>();

    public Branch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getBookIsbns() {
        return bookIsbns;
    }

    public void addBook(String isbn) {
        bookIsbns.add(isbn);
    }

    public void removeBook(String isbn) {
        bookIsbns.remove(isbn);
    }

    @Override
    public String toString() {
        return name + " (Books: " + bookIsbns + ")";
    }
}


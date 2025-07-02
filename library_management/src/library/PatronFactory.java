package library;

public class PatronFactory {
    public static Patron createPatron(String name, String id) {
        return new Patron(name, id);
    }
}


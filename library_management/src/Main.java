import library.*;

import java.util.*;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Book> books = new ArrayList<>(Arrays.asList(
                new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937),
                new Book("1984", "George Orwell", "978-0451524935", 1949)
        ));
        List<Patron> patrons = new ArrayList<>(Arrays.asList(
                new Patron("Alice Smith", "P001"),
                new Patron("Bob Johnson", "P002")
        ));
        List<Branch> branches = new ArrayList<>(Arrays.asList(
            new Branch("Main"),
            new Branch("East"),
            new Branch("West")
        ));
        Set<String> borrowedIsbns = new HashSet<>();
        Map<String, Queue<String>> reservations = new HashMap<>();
        ReservationObserver observer = new CLIReservationObserver();
        for (Book b : books) {
            branches.get(0).addBook(b.getIsbn());
            b.setBranch("Main");
        }
        logger.info("Library Management CLI started.");
        while (true) {
            System.out.println("\nLibrary Management CLI");
            System.out.println("1. List Books");
            System.out.println("2. List Patrons");
            System.out.println("3. Add library.Book");
            System.out.println("4. Remove library.Book");
            System.out.println("5. Update library.Book");
            System.out.println("6. Search Books");
            System.out.println("7. Add library.Patron");
            System.out.println("8. Update library.Patron");
            System.out.println("9. Check Out library.Book");
            System.out.println("10. Return library.Book");
            System.out.println("11. List Branches");
            System.out.println("12. List Books in library.Branch");
            System.out.println("13. Transfer library.Book Between Branches");
            System.out.println("14. Reserve library.Book");
            System.out.println("15. View library.Book Reservations");
            System.out.println("16. Recommend Books for library.Patron");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\nBooks:");
                    for (Book b : books) System.out.println(b);
                    break;
                case "2":
                    System.out.println("\nPatrons:");
                    for (Patron p : patrons) System.out.println(p);
                    break;
                case "3":
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter publication year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    books.add(BookFactory.createBook(title, author, isbn, year));
                    logger.info("library.Book added: " + title + ", " + author + ", " + isbn);
                    System.out.println("library.Book added.");
                    break;
                case "4":
                    System.out.print("Enter ISBN of book to remove: ");
                    String remIsbn = scanner.nextLine();
                    books.removeIf(b -> b.getIsbn().equals(remIsbn));
                    logger.info("library.Book removed: " + remIsbn);
                    System.out.println("library.Book removed if ISBN matched.");
                    break;
                case "5":
                    System.out.print("Enter ISBN of book to update: ");
                    String updIsbn = scanner.nextLine();
                    for (Book b : books) {
                        if (b.getIsbn().equals(updIsbn)) {
                            System.out.print("Enter new title: ");
                            String newTitle = scanner.nextLine();
                            System.out.print("Enter new author: ");
                            String newAuthor = scanner.nextLine();
                            System.out.print("Enter new publication year: ");
                            int newYear = Integer.parseInt(scanner.nextLine());
                            books.set(books.indexOf(b), BookFactory.createBook(newTitle, newAuthor, updIsbn, newYear));
                            logger.info("library.Book updated: " + updIsbn);
                            System.out.println("library.Book updated.");
                            break;
                        }
                    }
                    break;
                case "6":
                    System.out.print("Search by (title/author/isbn): ");
                    String field = scanner.nextLine();
                    System.out.print("Enter search value: ");
                    String value = scanner.nextLine();
                    System.out.println("Results:");
                    for (Book b : books) {
                        if ((field.equalsIgnoreCase("title") && b.getTitle().equalsIgnoreCase(value)) ||
                            (field.equalsIgnoreCase("author") && b.getAuthor().equalsIgnoreCase(value)) ||
                            (field.equalsIgnoreCase("isbn") && b.getIsbn().equalsIgnoreCase(value))) {
                            System.out.println(b);
                        }
                    }
                    break;
                case "7":
                    System.out.print("Enter patron name: ");
                    String pname = scanner.nextLine();
                    System.out.print("Enter patron ID: ");
                    String pid = scanner.nextLine();
                    patrons.add(PatronFactory.createPatron(pname, pid));
                    logger.info("library.Patron added: " + pname + ", " + pid);
                    System.out.println("library.Patron added.");
                    break;
                case "8":
                    System.out.print("Enter patron ID to update: ");
                    String upid = scanner.nextLine();
                    for (Patron p : patrons) {
                        if (p.getId().equals(upid)) {
                            System.out.print("Enter new name: ");
                            String newName = scanner.nextLine();
                            patrons.set(patrons.indexOf(p), new Patron(newName, upid));
                            System.out.println("library.Patron updated.");
                            break;
                        }
                    }
                    break;
                case "9":
                    System.out.print("Enter patron ID: ");
                    String patronId = scanner.nextLine();
                    Patron borrowingPatron = null;
                    for (Patron p : patrons) {
                        if (p.getId().equals(patronId)) {
                            borrowingPatron = p;
                            break;
                        }
                    }
                    if (borrowingPatron == null) {
                        System.out.println("library.Patron not found.");
                        break;
                    }
                    System.out.print("Enter ISBN of book to check out: ");
                    String borrowIsbn = scanner.nextLine();
                    boolean foundBook = false;
                    for (Book b : books) {
                        if (b.getIsbn().equals(borrowIsbn)) {
                            foundBook = true;
                            if (borrowedIsbns.contains(borrowIsbn)) {
                                System.out.println("library.Book is already borrowed.");
                            } else {
                                borrowedIsbns.add(borrowIsbn);
                                borrowingPatron.borrowBook(borrowIsbn);
                                System.out.println("library.Book checked out.");
                            }
                            break;
                        }
                    }
                    if (!foundBook) System.out.println("library.Book not found.");
                    break;
                case "10":
                    System.out.print("Enter patron ID: ");
                    String returnPatronId = scanner.nextLine();
                    Patron returningPatron = null;
                    for (Patron p : patrons) {
                        if (p.getId().equals(returnPatronId)) {
                            returningPatron = p;
                            break;
                        }
                    }
                    if (returningPatron == null) {
                        System.out.println("library.Patron not found.");
                        break;
                    }
                    System.out.print("Enter ISBN of book to return: ");
                    String returnIsbn = scanner.nextLine();
                    if (returningPatron.getBorrowedBookIsbns().contains(returnIsbn)) {
                        returningPatron.returnBook(returnIsbn);
                        borrowedIsbns.remove(returnIsbn);
                        System.out.println("library.Book returned.");
                        // Notify next patron in reservation queue
                        Queue<String> notifyQueue = reservations.get(returnIsbn);
                        if (notifyQueue != null && !notifyQueue.isEmpty()) {
                            String nextPatronId = notifyQueue.poll();
                            System.out.println("Notification: library.Patron " + nextPatronId + " can now check out book " + returnIsbn + ".");
                        }
                    } else {
                        System.out.println("This patron did not borrow this book.");
                    }
                    break;
                case "11":
                    System.out.println("\nBranches:");
                    for (Branch br : branches) System.out.println(br);
                    break;
                case "12":
                    System.out.print("Enter branch name: ");
                    String branchName = scanner.nextLine();
                    Branch foundBranch = null;
                    for (Branch br : branches) {
                        if (br.getName().equalsIgnoreCase(branchName)) {
                            foundBranch = br;
                            break;
                        }
                    }
                    if (foundBranch == null) {
                        System.out.println("library.Branch not found.");
                        break;
                    }
                    System.out.println("Books in branch " + branchName + ":");
                    for (String branchIsbn : foundBranch.getBookIsbns()) {
                        for (Book b : books) {
                            if (b.getIsbn().equals(branchIsbn)) System.out.println(b);
                        }
                    }
                    break;
                case "13":
                    System.out.print("Enter ISBN of book to transfer: ");
                    String transferIsbn = scanner.nextLine();
                    System.out.print("Enter destination branch name: ");
                    String destBranchName = scanner.nextLine();
                    Branch destBranch = null;
                    Branch srcBranch = null;
                    Book transferBook = null;
                    for (Book b : books) {
                        if (b.getIsbn().equals(transferIsbn)) {
                            transferBook = b;
                            break;
                        }
                    }
                    if (transferBook == null) {
                        System.out.println("library.Book not found.");
                        break;
                    }
                    for (Branch br : branches) {
                        if (br.getName().equalsIgnoreCase(destBranchName)) destBranch = br;
                        if (br.getName().equalsIgnoreCase(transferBook.getBranch())) srcBranch = br;
                    }
                    if (destBranch == null || srcBranch == null) {
                        System.out.println("library.Branch not found.");
                        break;
                    }
                    if (srcBranch == destBranch) {
                        System.out.println("library.Book is already in that branch.");
                        break;
                    }
                    srcBranch.removeBook(transferIsbn);
                    destBranch.addBook(transferIsbn);
                    transferBook.setBranch(destBranch.getName());
                    System.out.println("library.Book transferred.");
                    break;
                case "14":
                    System.out.print("Enter patron ID: ");
                    String resPatronId = scanner.nextLine();
                    Patron resPatron = null;
                    for (Patron p : patrons) {
                        if (p.getId().equals(resPatronId)) {
                            resPatron = p;
                            break;
                        }
                    }
                    if (resPatron == null) {
                        System.out.println("library.Patron not found.");
                        break;
                    }
                    System.out.print("Enter ISBN of book to reserve: ");
                    String resIsbn = scanner.nextLine();
                    boolean bookExists = false;
                    for (Book b : books) {
                        if (b.getIsbn().equals(resIsbn)) {
                            bookExists = true;
                            break;
                        }
                    }
                    if (!bookExists) {
                        System.out.println("library.Book not found.");
                        break;
                    }
                    reservations.putIfAbsent(resIsbn, new LinkedList<>());
                    Queue<String> queue = reservations.get(resIsbn);
                    if (queue.contains(resPatronId)) {
                        System.out.println("You have already reserved this book.");
                    } else {
                        queue.add(resPatronId);
                        System.out.println("library.Book reserved. You are number " + queue.size() + " in the queue.");
                    }
                    break;
                case "15":
                    System.out.print("Enter ISBN of book to view reservations: ");
                    String viewIsbn = scanner.nextLine();
                    Queue<String> resQueue = reservations.get(viewIsbn);
                    if (resQueue == null || resQueue.isEmpty()) {
                        System.out.println("No reservations for this book.");
                    } else {
                        System.out.println("Reservation queue for book " + viewIsbn + ": " + resQueue);
                    }
                    break;
                case "16":
                    System.out.print("Enter patron ID: ");
                    String recPatronId = scanner.nextLine();
                    Patron recPatron = null;
                    for (Patron p : patrons) {
                        if (p.getId().equals(recPatronId)) {
                            recPatron = p;
                            break;
                        }
                    }
                    if (recPatron == null) {
                        System.out.println("library.Patron not found.");
                        break;
                    }
                    Set<String> borrowedAuthors = new HashSet<>();
                    Set<String> borrowedBranches = new HashSet<>();
                    for (String historyIsbn : recPatron.getBorrowingHistory()) {
                        for (Book b : books) {
                            if (b.getIsbn().equals(historyIsbn)) {
                                borrowedAuthors.add(b.getAuthor());
                                borrowedBranches.add(b.getBranch());
                            }
                        }
                    }
                    List<Book> recommendations = new ArrayList<>();
                    for (Book b : books) {
                        if (!recPatron.getBorrowedBookIsbns().contains(b.getIsbn()) &&
                            !recPatron.getBorrowingHistory().contains(b.getIsbn()) &&
                            (borrowedAuthors.contains(b.getAuthor()) || borrowedBranches.contains(b.getBranch()))) {
                            recommendations.add(b);
                        }
                    }
                    if (recommendations.isEmpty()) {
                        System.out.println("No recommendations available based on your history.");
                    } else {
                        System.out.println("Recommended books:");
                        for (Book b : recommendations) System.out.println(b);
                    }
                    break;
                case "0":
                    System.out.println("Exiting.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
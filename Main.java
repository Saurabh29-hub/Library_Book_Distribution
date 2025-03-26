import java.io.*;
import java.util.*;

class Book {
    int id;
    String title;
    String author;
    boolean isBorrowed;

    public Book(int id, String title, String author, boolean isBorrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Borrowed: " + isBorrowed;
    }
}

public class Main {
    static List<Book> books = new ArrayList<>();
    static final String FILE_NAME = "books.txt";

    public static void main(String[] args) {
        loadBooksFromFile();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Library Menu ===");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayBooks();
                    break;
                case "2":
                    borrowBook(scanner);
                    break;
                case "3":
                    saveBooksToFile();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    static void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Invalid line skipped: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    boolean borrowed = Boolean.parseBoolean(parts[3].trim());

                    books.add(new Book(id, title, author, borrowed));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                bw.write(book.id + "," + book.title + "," + book.author + "," + book.isBorrowed);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    static void displayBooks() {
        System.out.println("\nLibrary Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static void borrowBook(Scanner scanner) {
        System.out.print("Enter Book ID to borrow: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            boolean found = false;
            for (Book book : books) {
                if (book.id == id) {
                    if (!book.isBorrowed) {
                        book.isBorrowed = true;
                        System.out.println("You borrowed: " + book.title);
                    } else {
                        System.out.println("Book already borrowed.");
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Book ID not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID input.");
        }
    }
}

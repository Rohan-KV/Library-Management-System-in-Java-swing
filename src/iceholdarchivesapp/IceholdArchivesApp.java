/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package iceholdarchivesapp;

/**
 *
 * @author Rohan
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return title + "," + author;
    }
}

class Library {
    private List<Book> books;
    private String dataFilePath;

    public Library(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.books = loadBooksFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public void deleteBook(Book book) {
        books.remove(book);
        saveBooksToFile();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void displayBooks() {
        System.out.println("\nLibrary Books:");
        for (Book book : books) {
            System.out.println("Title: " + book.getTitle() + "\tAuthor: " + book.getAuthor());
        }
    }

    private List<Book> loadBooksFromFile() {
        List<Book> loadedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    loadedBooks.add(new Book(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedBooks;
    }

    private void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath))) {
            for (Book book : books) {
                writer.println(book.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> loadBorrowedBooks(String userDataFilePath) {
        List<Book> borrowedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    borrowedBooks.add(new Book(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    public static void saveBorrowedBooks(List<Book> borrowedBooks, String userDataFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(userDataFilePath))) {
            for (Book book : borrowedBooks) {
                writer.println(book.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class IceholdArchivesApp {
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new IceholdArchivesApp();
        });
    }

    public IceholdArchivesApp() {
        frame = new JFrame("The Icehold Archives");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome to The Icehold Archives");
        welcomeLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(welcomeLabel);

        JLabel loginLabel = new JLabel("Please login");
        loginLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(loginLabel);

        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminLoginDialog();
            }
        });
        frame.add(adminButton);

        JButton userButton = new JButton("User");
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserLoginDialog();
            }
        });
        frame.add(userButton);

        frame.setVisible(true);
    }

    private void showAdminLoginDialog() {
        JFrame adminLoginFrame = new JFrame("Admin Login");
        adminLoginFrame.setSize(300, 150);
        adminLoginFrame.setLayout(new GridLayout(3, 2, 10, 10));
        adminLoginFrame.setLocationRelativeTo(frame);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Helvetica", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Helvetica", Font.PLAIN, 16));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAdminLogin(usernameField.getText(), new String(passwordField.getPassword()));
                adminLoginFrame.dispose();
            }
        });

        adminLoginFrame.add(usernameLabel);
        adminLoginFrame.add(usernameField);
        adminLoginFrame.add(passwordLabel);
        adminLoginFrame.add(passwordField);
        adminLoginFrame.add(loginButton);

        adminLoginFrame.setVisible(true);
    }

    private void checkAdminLogin(String username, String password) {
        if (username.equals("Stephy") && password.equals("1234")) {
            JOptionPane.showMessageDialog(frame, "Admin login successful!");
            openAdminInterface();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid admin credentials. Please try again.");
        }
    }

    private void openAdminInterface() {
        frame.dispose();  

        Library library = new Library("library_data.txt");
        JFrame adminFrame = new JFrame("Admin Interface");
        adminFrame.setSize(800, 600);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JButton displayBooksButton = new JButton("Display Books");
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton backButton = new JButton("Back");
        JTextArea bookTextArea = new JTextArea(10, 30);
        bookTextArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(bookTextArea);

        displayBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookTextArea.setText(getBookDetails(library.getBooks()));
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose(); 
                frame.setVisible(true); 
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog("Enter book title:");
                String author = JOptionPane.showInputDialog("Enter book author:");
                Book newBook = new Book(title, author);
                library.addBook(newBook);
                bookTextArea.setText(getBookDetails(library.getBooks()));
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titleToDelete = JOptionPane.showInputDialog("Enter book title to delete:");
                for (Book book : library.getBooks()) {
                    if (book.getTitle().equalsIgnoreCase(titleToDelete)) {
                        library.deleteBook(book);
                        bookTextArea.setText(getBookDetails(library.getBooks()));
                        break;
                    }
                }
            }
        });
 
        panel.add(displayBooksButton);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(scrollPane);
        panel.add(backButton);
        adminFrame.add(panel);
        adminFrame.setVisible(true);
    }

    private void showUserLoginDialog() {
        JFrame userLoginFrame = new JFrame("User Login");
        userLoginFrame.setSize(300, 150);
        userLoginFrame.setLayout(new GridLayout(3, 2, 10, 10));
        userLoginFrame.setLocationRelativeTo(frame);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Helvetica", Font.PLAIN, 16));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkUserLogin(usernameField.getText());
                userLoginFrame.dispose();
            }
        });

        userLoginFrame.add(usernameLabel);
        userLoginFrame.add(usernameField);
        userLoginFrame.add(loginButton);

        userLoginFrame.setVisible(true);
    }

    private void checkUserLogin(String username) {
        if (username != null && !username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "User " + username + " login successful!");
            openUserInterface(username);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username. Please try again.");
        }
    }

    private void openUserInterface(String username) {
        frame.dispose();  // Close the main window

        Library library = new Library("library_data.txt");
        List<Book> borrowedBooks = Library.loadBorrowedBooks("user_data.txt");

        JFrame userFrame = new JFrame("User Interface");
        userFrame.setSize(800, 600);
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        JButton backButtonu = new  JButton("Back");
        JTextArea borrowedBooksTextArea = new JTextArea(10, 30);
        borrowedBooksTextArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTextArea);

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titleToBorrow = JOptionPane.showInputDialog("Enter book title to borrow:");
                for (Book book : library.getBooks()) {
                    if (book.getTitle().equalsIgnoreCase(titleToBorrow)) {
                        borrowedBooks.add(book);
                        library.deleteBook(book);
                        borrowedBooksTextArea.setText(getBookDetails(borrowedBooks));
                        Library.saveBorrowedBooks(borrowedBooks, "user_data.txt");
                        break;
                    }
                }
            }
        });
        backButtonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userFrame.dispose(); 
                frame.setVisible(true);
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titleToReturn = JOptionPane.showInputDialog("Enter book title to return:");
                for (Book book : borrowedBooks) {
                    if (book.getTitle().equalsIgnoreCase(titleToReturn)) {
                        library.addBook(book);
                        borrowedBooks.remove(book);
                        borrowedBooksTextArea.setText(getBookDetails(borrowedBooks));
                        Library.saveBorrowedBooks(borrowedBooks, "user_data.txt");
                        break;
                    }
                }
            }
        });

        panel.add(borrowButton);
        panel.add(returnButton);
        panel.add(scrollPane);
        panel.add(backButtonu);
        userFrame.add(panel);
        userFrame.setVisible(true);
    }

    private String getBookDetails(List<Book> books) {
        StringBuilder details = new StringBuilder();
        for (Book book : books) {
            details.append("Title: ").append(book.getTitle()).append("\tAuthor: ").append(book.getAuthor()).append("\n");
        }
        return details.toString();
    }
}

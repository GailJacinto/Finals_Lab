package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class BookBridgeMainFrame extends JFrame {
    private final LibraryService service = new LibraryService();
    private JTable bookTable, memberTable, loanTable, reservationTable;
    private DefaultTableModel bookModel, memberModel, loanModel, reservationModel;

    public BookBridgeMainFrame() {
        setTitle("BookBridge - Integrated Library System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DatabaseConnection.initializeDatabase();
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel catalogPanel = new JPanel(new BorderLayout());
        bookModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Category", "Total", "Available"}, 0);
        bookTable = new JTable(bookModel);
        catalogPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        JPanel bookActionPanel = new JPanel();
        JButton refreshBooksBtn = new JButton("Refresh Catalog");
        JButton addBookBtn = new JButton("Add Book");
        bookActionPanel.add(refreshBooksBtn);
        bookActionPanel.add(addBookBtn);
        catalogPanel.add(bookActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Catalog", catalogPanel);

        JPanel memberPanel = new JPanel(new BorderLayout());
        memberModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Type"}, 0);
        memberTable = new JTable(memberModel);
        memberPanel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        JPanel memberActionPanel = new JPanel();
        JButton refreshMembersBtn = new JButton("Refresh Members");
        JButton addMemberBtn = new JButton("Register Member");
        memberActionPanel.add(refreshMembersBtn);
        memberActionPanel.add(addMemberBtn);
        memberPanel.add(memberActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Members", memberPanel);

        JPanel loanPanel = new JPanel(new BorderLayout());
        loanModel = new DefaultTableModel(new String[]{"Loan ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Return Date", "Status", "Fine"}, 0);
        loanTable = new JTable(loanModel);
        loanPanel.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        JPanel loanActionPanel = new JPanel();
        JButton refreshLoansBtn = new JButton("Refresh Loans");
        JButton issueBookBtn = new JButton("Issue Book");
        JButton returnBookBtn = new JButton("Return Book");
        loanActionPanel.add(refreshLoansBtn);
        loanActionPanel.add(issueBookBtn);
        loanActionPanel.add(returnBookBtn);
        loanPanel.add(loanActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Circulation", loanPanel);

        JPanel resPanel = new JPanel(new BorderLayout());
        reservationModel = new DefaultTableModel(new String[]{"Res ID", "Book ID", "Member ID", "Date", "Status"}, 0);
        reservationTable = new JTable(reservationModel);
        resPanel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        JPanel resActionPanel = new JPanel();
        JButton refreshResBtn = new JButton("Refresh Reservations");
        JButton addResBtn = new JButton("Reserve Book");
        resActionPanel.add(refreshResBtn);
        resActionPanel.add(addResBtn);
        resPanel.add(resActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Reservations", resPanel);

        add(tabbedPane);

        refreshBooksBtn.addActionListener(e -> loadCatalogData());
        refreshMembersBtn.addActionListener(e -> loadMemberData());
        refreshLoansBtn.addActionListener(e -> loadLoanData());
        refreshResBtn.addActionListener(e -> loadReservationData());

        addBookBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
            if (title == null) return; 
            
            if (title.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Book title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            String author = JOptionPane.showInputDialog(this, "Enter Author:");
            if (author == null) return;
            
            String category = JOptionPane.showInputDialog(this, "Enter Category:");
            if (category == null) return;
            
            String copiesStr = JOptionPane.showInputDialog(this, "Enter Total Copies:");
            if (copiesStr == null) return;

            try {
                service.addNewBook(title, author, category, Integer.parseInt(copiesStr));
                loadCatalogData();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addMemberBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Name:");
            String email = JOptionPane.showInputDialog(this, "Enter Email:");
            String phone = JOptionPane.showInputDialog(this, "Enter Phone:");
            String type = JOptionPane.showInputDialog(this, "Enter Membership Type (Student/Faculty):");
            try {
                service.registerMember(name, email, phone, type);
                loadMemberData();
                JOptionPane.showMessageDialog(this, "Member registered successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        issueBookBtn.addActionListener(e -> {
            String bIdStr = JOptionPane.showInputDialog(this, "Enter Book ID:");
            String mIdStr = JOptionPane.showInputDialog(this, "Enter Member ID:");
            try {
                service.issueBook(Integer.parseInt(bIdStr), Integer.parseInt(mIdStr), LocalDate.now().toString(), LocalDate.now().plusDays(14).toString());
                loadLoanData();
                loadCatalogData();
                JOptionPane.showMessageDialog(this, "Book issued successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnBookBtn.addActionListener(e -> {
            String lIdStr = JOptionPane.showInputDialog(this, "Enter Loan ID:");
            String bIdStr = JOptionPane.showInputDialog(this, "Enter Book ID:");
            try {
                service.returnBook(Integer.parseInt(lIdStr), Integer.parseInt(bIdStr), LocalDate.now().toString());
                loadLoanData();
                loadCatalogData();
                JOptionPane.showMessageDialog(this, "Book returned successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addResBtn.addActionListener(e -> {
            String bIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to Reserve:");
            String mIdStr = JOptionPane.showInputDialog(this, "Enter Member ID:");
            try {
                service.reserveBook(Integer.parseInt(bIdStr), Integer.parseInt(mIdStr), LocalDate.now().toString());
                loadReservationData();
                JOptionPane.showMessageDialog(this, "Reservation placed!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadCatalogData();
        loadMemberData();
        loadLoanData();
        loadReservationData();
    }

    private void loadCatalogData() {
        try {
            List<Book> books = service.getCatalog();
            bookModel.setRowCount(0);
            for (Book b : books) {
                bookModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getTotalCopies(), b.getAvailableCopies()});
            }
        } catch (BookBridgeException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void loadMemberData() {
        try {
            List<Member> members = service.getMembers();
            memberModel.setRowCount(0);
            for (Member m : members) {
                memberModel.addRow(new Object[]{m.getMemberId(), m.getName(), m.getEmail(), m.getPhone(), m.getMembershipType()});
            }
        } catch (BookBridgeException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void loadLoanData() {
        try {
            List<Loan> loans = service.getLoans();
            loanModel.setRowCount(0);
            for (Loan l : loans) {
                loanModel.addRow(new Object[]{l.getLoanId(), l.getBookId(), l.getMemberId(), l.getBorrowDate(), l.getDueDate(), l.getReturnDate(), l.getStatus(), l.getFineAmount()});
            }
        } catch (BookBridgeException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void loadReservationData() {
        try {
            List<Reservation> list = service.getReservations();
            reservationModel.setRowCount(0);
            for (Reservation r : list) {
                reservationModel.addRow(new Object[]{r.getReservationId(), r.getBookId(), r.getMemberId(), r.getReservationDate(), r.getStatus()});
            }
        } catch (BookBridgeException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookBridgeMainFrame().setVisible(true));
    }
}
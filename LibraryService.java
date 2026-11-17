package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final CirculationDAO circulationDAO = new CirculationDAO();

    public List<Book> getCatalog() throws BookBridgeException {
        try { return bookDAO.getAllBooks(); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to retrieve catalog", e); }
    }

    public void addNewBook(String title, String author, String category, int copies) throws BookBridgeException {
        if (title == null || title.trim().isEmpty()) {
            throw new BookBridgeException("Book title cannot be empty.");
        }
        try {
            bookDAO.addBook(new Book(0, title, author, category, copies, copies));
        } catch (SQLException e) {
            throw new BookBridgeException("Failed to add book", e);
        }
    }

    public List<Member> getMembers() throws BookBridgeException {
        try { return memberDAO.getAllMembers(); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to retrieve members", e); }
    }

    public void registerMember(String name, String email, String phone, String type) throws BookBridgeException {
        if (email == null || !email.contains("@")) {
            throw new BookBridgeException("Invalid email address.");
        }
        try {
            memberDAO.addMember(new Member(0, name, email, phone, type));
        } catch (SQLException e) {
            throw new BookBridgeException("Failed to register member (Email may already exist)", e);
        }
    }

    public void issueBook(int bookId, int memberId, String borrowDate, String dueDate) throws BookBridgeException {
        try {
            List<Book> catalog = getCatalog();
            Book targetBook = null;
            for (Book b : catalog) {
                if (b.getBookId() == bookId) {
                    targetBook = b;
                    break;
                }
            }

            if (targetBook == null) {
                throw new BookBridgeException("Book ID not found.");
            }
            if (targetBook.getAvailableCopies() <= 0) {
                throw new BookBridgeException("Cannot issue book. No available copies left (Out of stock).");
            }

            circulationDAO.issueBook(bookId, memberId, borrowDate, dueDate);
        } catch (SQLException e) { 
            throw new BookBridgeException("Failed to issue book. Check ID or availability.", e); 
        }
    }

    public void returnBook(int loanId, int bookId, String returnDate) throws BookBridgeException {
        try { 
            
            for (Loan l : getLoans()) {
                if (l.getLoanId() == loanId && "RETURNED".equals(l.getStatus())) {
                    throw new BookBridgeException("This book has already been returned.");
                }
            }
            
            circulationDAO.returnBook(loanId, bookId, returnDate); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to process return.", e); }
    }

    public List<Loan> getLoans() throws BookBridgeException {
        try { return circulationDAO.getAllLoans(); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to load loans.", e); }
    }

    public void reserveBook(int bookId, int memberId, String date) throws BookBridgeException {
        try { circulationDAO.addReservation(new Reservation(0, bookId, memberId, date, "PENDING")); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to place reservation.", e); }
    }

    public List<Reservation> getReservations() throws BookBridgeException {
        try { return circulationDAO.getAllReservations(); } 
        catch (SQLException e) { throw new BookBridgeException("Failed to load reservations.", e); }
    }
}
package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CirculationDAO {
    public void issueBook(int bookId, int memberId, String borrowDate, String dueDate) throws SQLException {
        String sqlLoan = "INSERT INTO loans (book_id, member_id, borrow_date, due_date, status, fine_amount) VALUES (?, ?, ?, ?, 'ACTIVE', 0.0)";
        String updateBook = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlLoan);
                 PreparedStatement p2 = conn.prepareStatement(updateBook)) {
                p1.setInt(1, bookId);
                p1.setInt(2, memberId);
                p1.setString(3, borrowDate);
                p1.setString(4, dueDate);
                p1.executeUpdate();

                p2.setInt(1, bookId);
                p2.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void returnBook(int loanId, int bookId, String returnDate) throws SQLException {
        String sqlLoan = "UPDATE loans SET status = 'RETURNED', return_date = ? WHERE loan_id = ?";
        String updateBook = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlLoan);
                 PreparedStatement p2 = conn.prepareStatement(updateBook)) {
                p1.setString(1, returnDate);
                p1.setInt(2, loanId);
                p1.executeUpdate();

                p2.setInt(1, bookId);
                p2.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"), rs.getInt("book_id"), rs.getInt("member_id"),
                    rs.getString("borrow_date"), rs.getString("due_date"),
                    rs.getString("return_date"), rs.getString("status"), rs.getDouble("fine_amount")
                ));
            }
        }
        return loans;
    }

    public void addReservation(Reservation res) throws SQLException {
        String sql = "INSERT INTO reservations (book_id, member_id, reservation_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, res.getBookId());
            pstmt.setInt(2, res.getMemberId());
            pstmt.setString(3, res.getReservationDate());
            pstmt.setString(4, res.getStatus());
            pstmt.executeUpdate();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Reservation(
                    rs.getInt("reservation_id"), rs.getInt("book_id"),
                    rs.getInt("member_id"), rs.getString("reservation_date"), rs.getString("status")
                ));
            }
        }
        return list;
    }
}
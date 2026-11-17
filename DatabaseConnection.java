package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:bookbridge.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() {
        String createBooks = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, author TEXT NOT NULL, category TEXT NOT NULL, " +
                "total_copies INTEGER NOT NULL, available_copies INTEGER NOT NULL);";

        String createMembers = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, email TEXT UNIQUE NOT NULL, phone TEXT NOT NULL, " +
                "membership_type TEXT NOT NULL);";

        String createLoans = "CREATE TABLE IF NOT EXISTS loans (" +
                "loan_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book_id INTEGER NOT NULL, member_id INTEGER NOT NULL, " +
                "borrow_date TEXT NOT NULL, due_date TEXT NOT NULL, return_date TEXT, " +
                "status TEXT NOT NULL, fine_amount REAL DEFAULT 0.0, " +
                "FOREIGN KEY(book_id) REFERENCES books(book_id), " +
                "FOREIGN KEY(member_id) REFERENCES members(member_id));";

        String createReservations = "CREATE TABLE IF NOT EXISTS reservations (" +
                "reservation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book_id INTEGER NOT NULL, member_id INTEGER NOT NULL, " +
                "reservation_date TEXT NOT NULL, status TEXT NOT NULL, " +
                "FOREIGN KEY(book_id) REFERENCES books(book_id), " +
                "FOREIGN KEY(member_id) REFERENCES members(member_id));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createMembers);
            stmt.execute(createLoans);
            stmt.execute(createReservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
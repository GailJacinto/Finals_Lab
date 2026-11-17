package com.mycompany.jacinto_campusconnectfinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    private static final String URL = "jdbc:sqlite:campusconnect.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS service_requests (" +
                    "request_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_name TEXT NOT NULL, " +
                    "student_email TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "priority TEXT NOT NULL, " +
                    "estimate_hours REAL NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "created_date TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRequest(ServiceRequest req) throws SQLException {
        String sql = "INSERT INTO service_requests (student_name, student_email, category, priority, estimate_hours, status, created_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, req.getStudentName());
            pstmt.setString(2, req.getStudentEmail());
            pstmt.setString(3, req.getCategory());
            pstmt.setString(4, req.getPriority());
            pstmt.setDouble(5, req.getEstimateHours());
            pstmt.setString(6, req.getStatus());
            pstmt.setString(7, req.getCreatedDate());
            pstmt.executeUpdate();
        }
    }

    public List<ServiceRequest> getAllRequests() throws SQLException {
        List<ServiceRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM service_requests";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ServiceRequest(
                    rs.getInt("request_id"),
                    rs.getString("student_name"),
                    rs.getString("student_email"),
                    rs.getString("category"),
                    rs.getString("priority"),
                    rs.getDouble("estimate_hours"),
                    rs.getString("status"),
                    rs.getString("created_date")
                ));
            }
        }
        return list;
    }

    public void updateStatus(int requestId, String newStatus) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, requestId);
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Request ID not found.");
            }
        }
    }

    public void deleteRequest(int requestId) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE request_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestId);
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Request ID not found.");
            }
        }
    }
}
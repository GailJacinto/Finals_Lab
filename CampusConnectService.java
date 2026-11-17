package com.mycompany.jacinto_campusconnectfinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.SQLException;
import java.util.List;

public class CampusConnectService {
    private final RequestDAO dao = new RequestDAO();

    public void addNewRequest(String name, String email, String category, String priority, double hours) throws CampusConnectException {
        if (name == null || name.trim().isEmpty()) {
            throw new CampusConnectException("Student name cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new CampusConnectException("Invalid student email address.");
        }
        if (hours <= 0 || hours > 100) {
            throw new CampusConnectException("Estimate hours must be between 1 and 100.");
        }
        try {
            ServiceRequest req = new ServiceRequest(0, name.trim(), email.trim(), category, priority, hours, "PENDING", java.time.LocalDate.now().toString());
            dao.addRequest(req);
        } catch (SQLException e) {
            throw new CampusConnectException("Failed to save service request: " + e.getMessage(), e);
        }
    }

    public List<ServiceRequest> getRequests() throws CampusConnectException {
        try {
            return dao.getAllRequests();
        } catch (SQLException e) {
            throw new CampusConnectException("Failed to retrieve requests from database.", e);
        }
    }

    public void updateRequestStatus(int id, String status) throws CampusConnectException {
        try {
            dao.updateStatus(id, status);
        } catch (SQLException e) {
            throw new CampusConnectException("Failed to update status: " + e.getMessage(), e);
        }
    }

    public void removeRequest(int id) throws CampusConnectException {
        try {
            dao.deleteRequest(id);
        } catch (SQLException e) {
            throw new CampusConnectException("Failed to delete request: " + e.getMessage(), e);
        }
    }
}
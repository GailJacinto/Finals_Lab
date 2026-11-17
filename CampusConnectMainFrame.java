package com.mycompany.jacinto_campusconnectfinal;

/**
 *
 * @author Gail Jacinto
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CampusConnectMainFrame extends JFrame {
    private final CampusConnectService service = new CampusConnectService();
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public CampusConnectMainFrame() {
        setTitle("CampusConnect - Student Service Management System");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        RequestDAO.initializeDatabase();

        tableModel = new DefaultTableModel(new String[]{"ID", "Student Name", "Email", "Category", "Priority", "Est. Hours", "Status", "Date"}, 0);
        requestTable = new JTable(tableModel);
        add(new JScrollPane(requestTable), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add Request");
        JButton updateBtn = new JButton("Update Status");
        JButton deleteBtn = new JButton("Delete Request");
        JButton refreshBtn = new JButton("Refresh List");

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
                if (name == null) return;
                if (name.trim().isEmpty()) {
                    throw new CampusConnectException("Student name cannot be empty.");
                }

                String email = JOptionPane.showInputDialog(this, "Enter Student Email:");
                if (email == null) return;
                if (email.trim().isEmpty()) {
                    throw new CampusConnectException("Student email cannot be empty.");
                }

                String category = JOptionPane.showInputDialog(this, "Enter Category (Advising, Technical, Clearance):");
                if (category == null) return;

                String priority = JOptionPane.showInputDialog(this, "Enter Priority (High, Medium, Low):");
                if (priority == null) return;

                String hoursStr = JOptionPane.showInputDialog(this, "Enter Estimate Hours:");
                if (hoursStr == null) return;

                service.addNewRequest(name, email, category, priority, Double.parseDouble(hoursStr));
                loadData();
                JOptionPane.showMessageDialog(this, "Service request added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid numeric value for hours.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Request ID to Update:");
            if (idStr == null) return;
            String status = JOptionPane.showInputDialog(this, "Enter New Status (PENDING, IN_PROGRESS, COMPLETED):");
            if (status == null) return;
            try {
                service.updateRequestStatus(Integer.parseInt(idStr), status.toUpperCase());
                loadData();
                JOptionPane.showMessageDialog(this, "Status updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Request ID to Delete:");
            if (idStr == null) return;
            try {
                service.removeRequest(Integer.parseInt(idStr));
                loadData();
                JOptionPane.showMessageDialog(this, "Request deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> loadData());

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<ServiceRequest> requests = service.getRequests();
            for (ServiceRequest r : requests) {
                tableModel.addRow(new Object[]{
                    r.getRequestId(), r.getStudentName(), r.getStudentEmail(),
                    r.getCategory(), r.getPriority(), r.getEstimateHours(),
                    r.getStatus(), r.getCreatedDate()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
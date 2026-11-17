package com.mycompany.jacinto_campusconnectfinal;

/**
 *
 * @author Gail Jacinto
 */
public class ServiceRequest {
    private int requestId;
    private String studentName;
    private String studentEmail;
    private String category;
    private String priority;
    private double estimateHours;
    private String status;
    private String createdDate;

    public ServiceRequest(int requestId, String studentName, String studentEmail, String category, String priority, double estimateHours, String status, String createdDate) {
        this.requestId = requestId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.category = category;
        this.priority = priority;
        this.estimateHours = estimateHours;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getRequestId() { return requestId; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public double getEstimateHours() { return estimateHours; }
    public String getStatus() { return status; }
    public String getCreatedDate() { return createdDate; }
}
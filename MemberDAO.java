package com.mycompany.jacinto_bookbridgefinal;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                    rs.getInt("member_id"), rs.getString("name"),
                    rs.getString("email"), rs.getString("phone"),
                    rs.getString("membership_type")
                ));
            }
        }
        return members;
    }

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email, phone, membership_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getMembershipType());
            pstmt.executeUpdate();
        }
    }
}
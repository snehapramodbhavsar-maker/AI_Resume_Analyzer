package dao;

import database.DBConnection;
import model.Resume;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ResumeDAO.java
 * -------------------------------------------------
 * Data Access Object for 'resumes' table.
 * Handles save, fetch, and delete of resume records.
 * -------------------------------------------------
 */
public class ResumeDAO {

    private Connection conn;

    public ResumeDAO() {
        this.conn = DBConnection.getConnection();
    }

    /**
     * Save a new resume to the database.
     * @return generated resume ID, or -1 on failure
     */
    public int saveResume(Resume resume) {
        String sql = "INSERT INTO resumes (user_id, resume_title, resume_text) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, resume.getUserId());
            ps.setString(2, resume.getResumeTitle());
            ps.setString(3, resume.getResumeText());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                resume.setResumeId(generatedId);
                System.out.println("[ResumeDAO] Resume saved with ID: " + generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            System.err.println("[ResumeDAO] saveResume error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Fetch all resumes belonging to a specific user.
     */
    public List<Resume> getResumesByUser(int userId) {
        List<Resume> list = new ArrayList<>();
        String sql = "SELECT * FROM resumes WHERE user_id = ? ORDER BY uploaded_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Resume(
                    rs.getInt("resume_id"),
                    rs.getInt("user_id"),
                    rs.getString("resume_title"),
                    rs.getString("resume_text")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ResumeDAO] getResumesByUser error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Fetch a single resume by its ID.
     */
    public Resume getResumeById(int resumeId) {
        String sql = "SELECT * FROM resumes WHERE resume_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Resume(
                    rs.getInt("resume_id"),
                    rs.getInt("user_id"),
                    rs.getString("resume_title"),
                    rs.getString("resume_text")
                );
            }
        } catch (SQLException e) {
            System.err.println("[ResumeDAO] getResumeById error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Delete a resume by ID.
     */
    public boolean deleteResume(int resumeId) {
        String sql = "DELETE FROM resumes WHERE resume_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ResumeDAO] deleteResume error: " + e.getMessage());
            return false;
        }
    }
}

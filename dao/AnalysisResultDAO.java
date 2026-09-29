package dao;

import database.DBConnection;
import model.AnalysisResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AnalysisResultDAO.java
 * -------------------------------------------------
 * Data Access Object for 'analysis_results' table.
 * Handles saving and fetching analysis results.
 * -------------------------------------------------
 */
public class AnalysisResultDAO {

    private Connection conn;

    public AnalysisResultDAO() {
        this.conn = DBConnection.getConnection();
    }

    /**
     * Save analysis result to the database.
     * @return true if successful
     */
    public boolean saveResult(AnalysisResult result) {
        String sql = "INSERT INTO analysis_results " +
                     "(resume_id, score, matched_keywords, missing_keywords, suggestions) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, result.getResumeId());
            ps.setInt(2, result.getScore());
            ps.setString(3, result.getMatchedKeywords());
            ps.setString(4, result.getMissingKeywords());
            ps.setString(5, result.getSuggestions());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AnalysisResultDAO] saveResult error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetch the latest analysis result for a given resume.
     */
    public AnalysisResult getLatestResult(int resumeId) {
        String sql = "SELECT * FROM analysis_results WHERE resume_id = ? " +
                     "ORDER BY analyzed_at DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AnalysisResult ar = new AnalysisResult();
                ar.setResultId(rs.getInt("result_id"));
                ar.setResumeId(rs.getInt("resume_id"));
                ar.setScore(rs.getInt("score"));
                ar.setMatchedKeywords(rs.getString("matched_keywords"));
                ar.setMissingKeywords(rs.getString("missing_keywords"));
                ar.setSuggestions(rs.getString("suggestions"));
                return ar;
            }
        } catch (SQLException e) {
            System.err.println("[AnalysisResultDAO] getLatestResult error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Fetch all analysis history for a given resume.
     */
    public List<AnalysisResult> getAllResults(int resumeId) {
        List<AnalysisResult> list = new ArrayList<>();
        String sql = "SELECT * FROM analysis_results WHERE resume_id = ? ORDER BY analyzed_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AnalysisResult ar = new AnalysisResult();
                ar.setResultId(rs.getInt("result_id"));
                ar.setResumeId(rs.getInt("resume_id"));
                ar.setScore(rs.getInt("score"));
                ar.setMatchedKeywords(rs.getString("matched_keywords"));
                ar.setMissingKeywords(rs.getString("missing_keywords"));
                ar.setSuggestions(rs.getString("suggestions"));
                list.add(ar);
            }
        } catch (SQLException e) {
            System.err.println("[AnalysisResultDAO] getAllResults error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Fetch all keywords from the skills_keywords table.
     */
    public List<String> getAllKeywords() {
        List<String> keywords = new ArrayList<>();
        String sql = "SELECT keyword FROM skills_keywords";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                keywords.add(rs.getString("keyword").toLowerCase());
            }
        } catch (SQLException e) {
            System.err.println("[AnalysisResultDAO] getAllKeywords error: " + e.getMessage());
        }
        return keywords;
    }
}

package service;

import dao.ResumeDAO;
import model.Resume;

import java.util.List;

/**
 * ResumeService.java
 * -------------------------------------------------
 * Service layer for resume-related operations.
 * -------------------------------------------------
 */
public class ResumeService {

    private final ResumeDAO resumeDAO;

    public ResumeService() {
        this.resumeDAO = new ResumeDAO();
    }

    /**
     * Upload (save) a new resume.
     * @return resumeId on success, -1 on failure
     */
    public int uploadResume(int userId, String title, String text) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("❌ Resume title cannot be empty.");
            return -1;
        }
        if (text == null || text.trim().length() < 20) {
            System.out.println("❌ Resume text is too short.");
            return -1;
        }
        Resume resume = new Resume(userId, title.trim(), text.trim());
        return resumeDAO.saveResume(resume);
    }

    /**
     * Get all resumes for a user.
     */
    public List<Resume> getUserResumes(int userId) {
        return resumeDAO.getResumesByUser(userId);
    }

    /**
     * Get a specific resume by ID.
     */
    public Resume getResume(int resumeId) {
        return resumeDAO.getResumeById(resumeId);
    }

    /**
     * Delete a resume by ID.
     */
    public boolean deleteResume(int resumeId) {
        return resumeDAO.deleteResume(resumeId);
    }
}

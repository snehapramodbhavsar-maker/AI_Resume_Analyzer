package model;

/**
 * Resume.java
 * -------------------------------------------------
 * Model class representing the 'resumes' table.
 * -------------------------------------------------
 */
public class Resume {

    private int    resumeId;
    private int    userId;
    private String resumeTitle;
    private String resumeText;

    // ── Constructors ────────────────────────────────────────

    public Resume() {}

    public Resume(int userId, String resumeTitle, String resumeText) {
        this.userId      = userId;
        this.resumeTitle = resumeTitle;
        this.resumeText  = resumeText;
    }

    public Resume(int resumeId, int userId, String resumeTitle, String resumeText) {
        this.resumeId    = resumeId;
        this.userId      = userId;
        this.resumeTitle = resumeTitle;
        this.resumeText  = resumeText;
    }

    // ── Getters & Setters ───────────────────────────────────

    public int    getResumeId()               { return resumeId; }
    public void   setResumeId(int id)         { this.resumeId = id; }

    public int    getUserId()                 { return userId; }
    public void   setUserId(int id)           { this.userId = id; }

    public String getResumeTitle()            { return resumeTitle; }
    public void   setResumeTitle(String t)    { this.resumeTitle = t; }

    public String getResumeText()             { return resumeText; }
    public void   setResumeText(String text)  { this.resumeText = text; }

    @Override
    public String toString() {
        return "Resume{id=" + resumeId + ", title='" + resumeTitle + "', userId=" + userId + "}";
    }
}

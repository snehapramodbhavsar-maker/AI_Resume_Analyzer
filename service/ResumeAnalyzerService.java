package service;

import dao.AnalysisResultDAO;
import model.AnalysisResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ResumeAnalyzerService.java
 * -------------------------------------------------
 * Service layer containing the core "AI-like" logic
 * for analyzing resumes.
 *
 * Responsibilities:
 *  - Keyword matching against DB skill list
 *  - Score calculation
 *  - Generating improvement suggestions
 * -------------------------------------------------
 */
public class ResumeAnalyzerService {

    private final AnalysisResultDAO analysisDAO;

    public ResumeAnalyzerService() {
        this.analysisDAO = new AnalysisResultDAO();
    }

    /**
     * Main analysis method.
     * Analyzes resume text and returns an AnalysisResult object.
     *
     * @param resumeId   ID of the resume in DB
     * @param resumeText Full resume text
     * @return AnalysisResult with score, keywords, suggestions
     */
    public AnalysisResult analyzeResume(int resumeId, String resumeText) {
        // Fetch all keywords from the database
        List<String> allKeywords = analysisDAO.getAllKeywords();

        String lowerText = resumeText.toLowerCase();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        // ── Keyword Matching ────────────────────────────────
        for (String keyword : allKeywords) {
            if (lowerText.contains(keyword.toLowerCase())) {
                matched.add(keyword);
            } else {
                missing.add(keyword);
            }
        }

        // ── Score Calculation ───────────────────────────────
        int score = calculateScore(resumeText, matched, allKeywords.size());

        // ── Generate Suggestions ────────────────────────────
        String suggestions = generateSuggestions(resumeText, matched, missing, score);

        // ── Build Result Object ──────────────────────────────
        AnalysisResult result = new AnalysisResult(
            resumeId,
            score,
            String.join(", ", matched),
            String.join(", ", missing.subList(0, Math.min(10, missing.size()))), // top 10 missing
            suggestions
        );

        // ── Save to DB ───────────────────────────────────────
        analysisDAO.saveResult(result);

        return result;
    }

    /**
     * Score Calculation Algorithm:
     *  - Base keyword match score  : 60%
     *  - Resume length bonus       : 10%
     *  - Contact info bonus        : 10%
     *  - Education keyword bonus   : 10%
     *  - Action verb bonus         : 10%
     */
    private int calculateScore(String text, List<String> matched, int totalKeywords) {
        int score = 0;
        String lower = text.toLowerCase();

        // 1. Keyword Match Score (max 60 points)
        if (totalKeywords > 0) {
            score += (int) ((matched.size() / (double) totalKeywords) * 60);
        }

        // 2. Resume Length Check (max 10 points)
        int wordCount = text.split("\\s+").length;
        if (wordCount >= 300) score += 10;
        else if (wordCount >= 150) score += 5;

        // 3. Contact Info (max 10 points)
        if (lower.contains("@") || lower.contains("email")) score += 5;
        if (lower.contains("phone") || lower.contains("mobile") || lower.matches(".*\\d{10}.*")) score += 5;

        // 4. Education Keywords (max 10 points)
        String[] eduWords = {"bachelor", "master", "degree", "university", "college", "b.tech", "mba", "bsc"};
        for (String w : eduWords) {
            if (lower.contains(w)) { score += 10; break; }
        }

        // 5. Action Verbs (max 10 points)
        String[] verbs = {"developed", "designed", "implemented", "managed", "led", "built",
                          "created", "improved", "analyzed", "achieved", "delivered"};
        for (String v : verbs) {
            if (lower.contains(v)) { score += 10; break; }
        }

        return Math.min(score, 100); // Cap at 100
    }

    /**
     * Generates human-readable improvement suggestions
     * based on what's missing or weak in the resume.
     */
    private String generateSuggestions(String text, List<String> matched,
                                       List<String> missing, int score) {
        StringBuilder sb = new StringBuilder();
        String lower = text.toLowerCase();

        sb.append("=== AI RESUME SUGGESTIONS ===\n\n");

        // Score-based opening
        if (score >= 80) {
            sb.append("✅ Great resume! A few refinements could make it perfect.\n\n");
        } else if (score >= 50) {
            sb.append("⚠️  Your resume is decent but has room for improvement.\n\n");
        } else {
            sb.append("❌ Your resume needs significant improvement. Follow the tips below.\n\n");
        }

        // Missing key technical skills
        List<String> topMissing = missing.subList(0, Math.min(5, missing.size()));
        if (!topMissing.isEmpty()) {
            sb.append("📌 Consider adding these skills if you have them:\n");
            for (String kw : topMissing) {
                sb.append("   → ").append(kw).append("\n");
            }
            sb.append("\n");
        }

        // Contact Info
        if (!lower.contains("@") && !lower.contains("email")) {
            sb.append("📧 Add your email address — it's essential for recruiters.\n");
        }
        if (!lower.contains("phone") && !lower.contains("mobile")) {
            sb.append("📞 Include your phone number.\n");
        }
        if (!lower.contains("linkedin") && !lower.contains("github")) {
            sb.append("🔗 Add LinkedIn / GitHub profile links to stand out.\n");
        }

        // Education
        boolean hasEdu = false;
        for (String w : new String[]{"bachelor","master","degree","university","college","b.tech","mba"}) {
            if (lower.contains(w)) { hasEdu = true; break; }
        }
        if (!hasEdu) sb.append("🎓 Include your educational qualifications.\n");

        // Action verbs
        boolean hasVerbs = false;
        for (String v : new String[]{"developed","designed","built","led","managed","improved","created"}) {
            if (lower.contains(v)) { hasVerbs = true; break; }
        }
        if (!hasVerbs) sb.append("💡 Use strong action verbs (e.g. Developed, Led, Designed, Implemented).\n");

        // Summary section
        if (!lower.contains("summary") && !lower.contains("objective") && !lower.contains("profile")) {
            sb.append("📝 Add a professional summary/objective at the top.\n");
        }

        // Length
        int wordCount = text.split("\\s+").length;
        if (wordCount < 150) {
            sb.append("📄 Your resume is too short. Aim for 300–600 words.\n");
        } else if (wordCount > 1000) {
            sb.append("✂️  Your resume is too long. Try to keep it to 1–2 pages.\n");
        }

        // Projects / Experience
        if (!lower.contains("project") && !lower.contains("experience")) {
            sb.append("🛠️  Add project descriptions or work experience to showcase your skills.\n");
        }

        sb.append("\n💪 Matched Keywords (").append(matched.size()).append("): ").append(String.join(", ", matched));
        return sb.toString();
    }
}

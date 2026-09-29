package model;

/**
 * AnalysisResult.java
 * -------------------------------------------------
 * Model class representing the 'analysis_results' table.
 * Stores score, matched/missing keywords, and suggestions.
 * -------------------------------------------------
 */
public class AnalysisResult {

    private int    resultId;
    private int    resumeId;
    private int    score;              // Score out of 100
    private String matchedKeywords;   // Comma-separated
    private String missingKeywords;   // Comma-separated
    private String suggestions;       // Improvement text

    // ── Constructors ────────────────────────────────────────

    public AnalysisResult() {}

    public AnalysisResult(int resumeId, int score,
                          String matchedKeywords,
                          String missingKeywords,
                          String suggestions) {
        this.resumeId        = resumeId;
        this.score           = score;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
        this.suggestions     = suggestions;
    }

    // ── Getters & Setters ───────────────────────────────────

    public int    getResultId()                    { return resultId; }
    public void   setResultId(int id)              { this.resultId = id; }

    public int    getResumeId()                    { return resumeId; }
    public void   setResumeId(int id)              { this.resumeId = id; }

    public int    getScore()                       { return score; }
    public void   setScore(int s)                  { this.score = s; }

    public String getMatchedKeywords()             { return matchedKeywords; }
    public void   setMatchedKeywords(String m)     { this.matchedKeywords = m; }

    public String getMissingKeywords()             { return missingKeywords; }
    public void   setMissingKeywords(String m)     { this.missingKeywords = m; }

    public String getSuggestions()                 { return suggestions; }
    public void   setSuggestions(String s)         { this.suggestions = s; }

    /**
     * Returns a star rating string based on score.
     */
    public String getStarRating() {
        if (score >= 80) return "★★★★★ Excellent";
        if (score >= 60) return "★★★★☆ Good";
        if (score >= 40) return "★★★☆☆ Average";
        if (score >= 20) return "★★☆☆☆ Needs Work";
        return               "★☆☆☆☆ Poor";
    }

    @Override
    public String toString() {
        return "AnalysisResult{resumeId=" + resumeId + ", score=" + score + "}";
    }
}

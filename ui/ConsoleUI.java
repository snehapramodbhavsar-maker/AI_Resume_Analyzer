package ui;

import model.AnalysisResult;
import model.Resume;
import model.User;
import service.ResumeAnalyzerService;
import service.ResumeService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * ConsoleUI.java
 * -------------------------------------------------
 * Console-based User Interface for Resume Analyzer.
 * Handles all user interactions via terminal.
 *
 * Menu Structure:
 *   [Before Login]  → Login | Register | Exit
 *   [After Login]   → Upload Resume | View Resumes
 *                     Analyze Resume | View History | Logout
 * -------------------------------------------------
 */
public class ConsoleUI {

    private final Scanner              scanner;
    private final UserService          userService;
    private final ResumeService        resumeService;
    private final ResumeAnalyzerService analyzerService;

    private User currentUser = null; // Logged-in user

    public ConsoleUI() {
        this.scanner         = new Scanner(System.in);
        this.userService     = new UserService();
        this.resumeService   = new ResumeService();
        this.analyzerService = new ResumeAnalyzerService();
    }

    // ────────────────────────────────────────────────────────
    //  Entry Point
    // ────────────────────────────────────────────────────────

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showGuestMenu();
            } else {
                running = showUserMenu();
            }
        }
        System.out.println("\n👋 Thank you for using Resume Analyzer. Goodbye!");
    }

    // ────────────────────────────────────────────────────────
    //  Guest Menu (not logged in)
    // ────────────────────────────────────────────────────────

    private boolean showGuestMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║         MAIN MENU            ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Login                    ║");
        System.out.println("║  2. Register                 ║");
        System.out.println("║  3. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": handleLogin();    break;
            case "2": handleRegister(); break;
            case "3": return false;
            default:  System.out.println("⚠️  Invalid choice. Try again.");
        }
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  User Menu (logged in)
    // ────────────────────────────────────────────────────────

    private boolean showUserMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.printf( "║  👤 Logged in: %-22s║%n", currentUser.getUsername());
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Upload New Resume                ║");
        System.out.println("║  2. View My Resumes                  ║");
        System.out.println("║  3. Analyze a Resume                 ║");
        System.out.println("║  4. View Analysis History            ║");
        System.out.println("║  5. Delete a Resume                  ║");
        System.out.println("║  6. Logout                           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": handleUploadResume();     break;
            case "2": handleViewResumes();      break;
            case "3": handleAnalyzeResume();    break;
            case "4": handleViewHistory();      break;
            case "5": handleDeleteResume();     break;
            case "6": currentUser = null;
                      System.out.println("✅ Logged out successfully."); break;
            default:  System.out.println("⚠️  Invalid choice. Try again.");
        }
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  Action Handlers
    // ────────────────────────────────────────────────────────

    private void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = userService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("✅ Welcome back, " + user.getUsername() + "!");
        } else {
            System.out.println("❌ Invalid credentials. Please try again.");
        }
    }

    private void handleRegister() {
        System.out.println("\n--- REGISTER ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Email   : ");
        String email = scanner.nextLine().trim();

        String result = userService.register(username, password, email);
        System.out.println(result);
    }

    private void handleUploadResume() {
        System.out.println("\n--- UPLOAD RESUME ---");
        System.out.print("Resume Title (e.g. 'Java Developer Resume'): ");
        String title = scanner.nextLine().trim();

        System.out.println("Paste your resume text below.");
        System.out.println("(Type 'DONE' on a new line when finished)");
        System.out.println("─".repeat(50));

        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("DONE")) {
            sb.append(line).append("\n");
        }

        String text = sb.toString().trim();
        if (text.isEmpty()) {
            System.out.println("❌ No text entered.");
            return;
        }

        int resumeId = resumeService.uploadResume(currentUser.getUserId(), title, text);
        if (resumeId > 0) {
            System.out.println("✅ Resume uploaded successfully! (ID: " + resumeId + ")");
            System.out.println("💡 Tip: Use option 3 to analyze it now.");
        } else {
            System.out.println("❌ Failed to save resume.");
        }
    }

    private void handleViewResumes() {
        System.out.println("\n--- MY RESUMES ---");
        List<Resume> resumes = resumeService.getUserResumes(currentUser.getUserId());

        if (resumes.isEmpty()) {
            System.out.println("📭 No resumes found. Upload one first (Option 1).");
            return;
        }

        System.out.println("┌─────┬──────────────────────────────────┐");
        System.out.println("│ ID  │ Title                            │");
        System.out.println("├─────┼──────────────────────────────────┤");
        for (Resume r : resumes) {
            System.out.printf("│ %-3d │ %-32s │%n", r.getResumeId(), r.getResumeTitle());
        }
        System.out.println("└─────┴──────────────────────────────────┘");
    }

    private void handleAnalyzeResume() {
        System.out.println("\n--- ANALYZE RESUME ---");
        List<Resume> resumes = resumeService.getUserResumes(currentUser.getUserId());

        if (resumes.isEmpty()) {
            System.out.println("📭 No resumes found. Upload one first.");
            return;
        }

        handleViewResumes();
        System.out.print("Enter Resume ID to analyze: ");
        int resumeId = readInt();

        Resume resume = resumeService.getResume(resumeId);
        if (resume == null || resume.getUserId() != currentUser.getUserId()) {
            System.out.println("❌ Resume not found or access denied.");
            return;
        }

        System.out.println("\n⏳ Analyzing resume... Please wait.");
        simulateLoading();

        AnalysisResult result = analyzerService.analyzeResume(resumeId, resume.getResumeText());
        printAnalysisResult(result, resume.getResumeTitle());
    }

    private void handleViewHistory() {
        System.out.println("\n--- ANALYSIS HISTORY ---");
        List<Resume> resumes = resumeService.getUserResumes(currentUser.getUserId());

        if (resumes.isEmpty()) {
            System.out.println("📭 No resumes found.");
            return;
        }

        handleViewResumes();
        System.out.print("Enter Resume ID to view history: ");
        int resumeId = readInt();

        dao.AnalysisResultDAO resultDAO = new dao.AnalysisResultDAO();
        List<AnalysisResult> history = resultDAO.getAllResults(resumeId);

        if (history.isEmpty()) {
            System.out.println("📭 No analysis history for this resume.");
            return;
        }

        System.out.println("\n📊 Analysis History:");
        for (int i = 0; i < history.size(); i++) {
            AnalysisResult ar = history.get(i);
            System.out.printf("[%d] Score: %d/100  %s%n", (i+1), ar.getScore(), ar.getStarRating());
        }
    }

    private void handleDeleteResume() {
        System.out.println("\n--- DELETE RESUME ---");
        handleViewResumes();
        System.out.print("Enter Resume ID to delete: ");
        int resumeId = readInt();

        System.out.print("⚠️  Are you sure? This will also delete all analysis results. (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            boolean deleted = resumeService.deleteResume(resumeId);
            System.out.println(deleted ? "✅ Resume deleted." : "❌ Delete failed.");
        } else {
            System.out.println("❌ Delete cancelled.");
        }
    }

    // ────────────────────────────────────────────────────────
    //  Display Helpers
    // ────────────────────────────────────────────────────────

    private void printAnalysisResult(AnalysisResult result, String title) {
        System.out.println("\n" + "═".repeat(55));
        System.out.println("       📄 RESUME ANALYSIS REPORT");
        System.out.println("═".repeat(55));
        System.out.printf("  Resume   : %s%n", title);
        System.out.printf("  Score    : %d / 100%n", result.getScore());
        System.out.printf("  Rating   : %s%n", result.getStarRating());
        System.out.println("─".repeat(55));

        // Score bar
        System.out.print("  Progress : [");
        int filled = result.getScore() / 5; // max 20 chars
        for (int i = 0; i < 20; i++) {
            System.out.print(i < filled ? "█" : "░");
        }
        System.out.println("] " + result.getScore() + "%");

        System.out.println("─".repeat(55));
        System.out.println("  ✅ Matched Skills:");
        if (result.getMatchedKeywords() != null && !result.getMatchedKeywords().isEmpty()) {
            for (String kw : result.getMatchedKeywords().split(", ")) {
                System.out.println("     ✔ " + kw);
            }
        } else {
            System.out.println("     None found.");
        }

        System.out.println("\n  ❌ Top Missing Skills:");
        if (result.getMissingKeywords() != null && !result.getMissingKeywords().isEmpty()) {
            for (String kw : result.getMissingKeywords().split(", ")) {
                System.out.println("     ✘ " + kw);
            }
        }

        System.out.println("\n" + result.getSuggestions());
        System.out.println("═".repeat(55));
        System.out.println("  ✅ Analysis saved to database.");
        System.out.println("═".repeat(55));
    }

    private void printBanner() {
        System.out.println("\n");
        System.out.println("  ██████╗ ███████╗███████╗██╗   ██╗███╗   ███╗███████╗");
        System.out.println("  ██╔══██╗██╔════╝██╔════╝██║   ██║████╗ ████║██╔════╝");
        System.out.println("  ██████╔╝█████╗  ███████╗██║   ██║██╔████╔██║█████╗  ");
        System.out.println("  ██╔══██╗██╔══╝  ╚════██║██║   ██║██║╚██╔╝██║██╔══╝  ");
        System.out.println("  ██║  ██║███████╗███████║╚██████╔╝██║ ╚═╝ ██║███████╗");
        System.out.println("  ╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝");
        System.out.println("         A N A L Y Z E R   v1.0  |  Java + MySQL");
        System.out.println("  ─────────────────────────────────────────────────────");
        System.out.println("  📌 Default login → username: admin  password: admin123");
        System.out.println("  ─────────────────────────────────────────────────────\n");
    }

    private void simulateLoading() {
        try {
            String[] frames = {"⠋","⠙","⠹","⠸","⠼","⠴","⠦","⠧","⠇","⠏"};
            for (int i = 0; i < 20; i++) {
                System.out.print("\r  " + frames[i % frames.length] + " Scanning keywords...");
                Thread.sleep(80);
            }
            System.out.println("\r  ✅ Analysis complete!             ");
        } catch (InterruptedException ignored) {}
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

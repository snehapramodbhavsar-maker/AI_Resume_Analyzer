package model;

/**
 * User.java
 * -------------------------------------------------
 * Model class representing the 'users' table.
 * Encapsulates user data using OOP principles.
 * -------------------------------------------------
 */
public class User {

    private int    userId;
    private String username;
    private String password;
    private String email;

    // ── Constructors ────────────────────────────────────────

    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    public User(int userId, String username, String password, String email) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    // ── Getters & Setters ───────────────────────────────────

    public int    getUserId()            { return userId; }
    public void   setUserId(int id)      { this.userId = id; }

    public String getUsername()          { return username; }
    public void   setUsername(String u)  { this.username = u; }

    public String getPassword()          { return password; }
    public void   setPassword(String p)  { this.password = p; }

    public String getEmail()             { return email; }
    public void   setEmail(String e)     { this.email = e; }

    @Override
    public String toString() {
        return "User{id=" + userId + ", username='" + username + "', email='" + email + "'}";
    }
}

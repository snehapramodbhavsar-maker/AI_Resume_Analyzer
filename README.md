# 📄 Resume Analyzer - Java OOP + JDBC Mini Project

A console-based Java application that mimics AI-powered resume analysis using
keyword matching, scoring logic, and improvement suggestions — backed by MySQL.

---

## 📁 Project Structure

```
ResumeAnalyzer/
├── Main.java                          ← Entry point
│
├── database/
│   ├── DBConnection.java              ← JDBC Singleton connection
│   └── resume_analyzer.sql            ← MySQL database & seed data
│
├── model/
│   ├── User.java                      ← User entity (POJO)
│   ├── Resume.java                    ← Resume entity (POJO)
│   └── AnalysisResult.java            ← Analysis result entity (POJO)
│
├── dao/
│   ├── UserDAO.java                   ← DB operations for users
│   ├── ResumeDAO.java                 ← DB operations for resumes
│   └── AnalysisResultDAO.java         ← DB operations for results + keywords
│
├── service/
│   ├── UserService.java               ← User business logic
│   ├── ResumeService.java             ← Resume business logic
│   └── ResumeAnalyzerService.java     ← Core AI-like analysis engine
│
└── ui/
    └── ConsoleUI.java                 ← Terminal menu interface
```

---

## 🗄️ OOP Concepts Used

| Concept        | Where Used |
|----------------|------------|
| Encapsulation  | All model classes (User, Resume, AnalysisResult) |
| Abstraction    | Service layer hides DB logic from UI |
| Inheritance    | (Extendable — DAO classes can share base) |
| Singleton      | DBConnection — one shared DB connection |
| Layered Architecture | UI → Service → DAO → Database |

---

## ⚙️ Setup Instructions

### Step 1: Database Setup
1. Open **MySQL Workbench** or terminal.
2. Run the SQL file:
   ```sql
   SOURCE /path/to/database/resume_analyzer.sql;
   ```
   This creates the database, tables, and seed data automatically.

### Step 2: Update DB Credentials
Open `database/DBConnection.java` and update:
```java
private static final String USER     = "root";      // your MySQL username
private static final String PASSWORD = "root";      // your MySQL password
```

### Step 3: Add MySQL JDBC Driver
Download `mysql-connector-j-x.x.x.jar` from:
https://dev.mysql.com/downloads/connector/j/

Place it in a `lib/` folder inside the project.

### Step 4: Compile
```bash
# From project root (Windows)
javac -cp ".;lib/mysql-connector-j.jar" database/*.java model/*.java dao/*.java service/*.java ui/*.java Main.java

# From project root (Mac/Linux)
javac -cp ".:lib/mysql-connector-j.jar" database/*.java model/*.java dao/*.java service/*.java ui/*.java Main.java
```

### Step 5: Run
```bash
# Windows
java -cp ".;lib/mysql-connector-j.jar" Main

# Mac/Linux
java -cp ".:lib/mysql-connector-j.jar" Main
```

---

## 🚀 Features

| Feature              | Description |
|----------------------|-------------|
| 🔐 User Auth         | Register & Login with validation |
| 📤 Upload Resume     | Paste resume text in terminal |
| 🔍 Keyword Analysis  | Matches 40+ skill keywords from DB |
| 📊 Score (0–100)     | Multi-factor scoring algorithm |
| ⭐ Star Rating       | Visual rating from ★☆☆☆☆ to ★★★★★ |
| 💡 Suggestions       | AI-feel personalized improvement tips |
| 🗂️ History           | View past analysis results per resume |
| 🗑️ Delete Resume     | Cascading delete removes analysis too |

---

## 🔑 Default Login

```
Username: admin
Password: admin123
```

---

## 📊 Scoring Algorithm

| Factor             | Max Points |
|--------------------|------------|
| Keyword Matches    | 60 pts     |
| Resume Length      | 10 pts     |
| Contact Info       | 10 pts     |
| Education Keywords | 10 pts     |
| Action Verbs       | 10 pts     |
| **Total**          | **100 pts**|

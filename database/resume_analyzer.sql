-- ============================================================
--  Resume Analyzer - MySQL Database Setup
--  File: resume_analyzer.sql
--  Run this file FIRST before launching the Java application
-- ============================================================

-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS resume_analyzer_db;
USE resume_analyzer_db;

-- ============================================================
-- Table 1: users
-- Stores application users (optional login)
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table 2: resumes
-- Stores uploaded resume text per user
-- ============================================================
CREATE TABLE IF NOT EXISTS resumes (
    resume_id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT          NOT NULL,
    resume_title    VARCHAR(100) NOT NULL,
    resume_text     LONGTEXT     NOT NULL,
    uploaded_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================================
-- Table 3: skills_keywords
-- Master list of skill keywords used for analysis
-- ============================================================
CREATE TABLE IF NOT EXISTS skills_keywords (
    keyword_id  INT AUTO_INCREMENT PRIMARY KEY,
    keyword     VARCHAR(100) NOT NULL UNIQUE,
    category    VARCHAR(50)  NOT NULL   -- e.g. Programming, Database, Soft Skills
);

-- ============================================================
-- Table 4: analysis_results
-- Stores analysis results for each resume
-- ============================================================
CREATE TABLE IF NOT EXISTS analysis_results (
    result_id           INT AUTO_INCREMENT PRIMARY KEY,
    resume_id           INT  NOT NULL,
    score               INT  NOT NULL,   -- Score out of 100
    matched_keywords    TEXT,            -- Comma-separated matched keywords
    missing_keywords    TEXT,            -- Comma-separated missing keywords
    suggestions         TEXT,            -- Improvement suggestions
    analyzed_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
);

-- ============================================================
-- Seed Data: Default User
-- ============================================================
INSERT IGNORE INTO users (username, password, email)
VALUES ('admin', 'admin123', 'admin@resumeanalyzer.com');

-- ============================================================
-- Seed Data: Skill Keywords (categorized)
-- ============================================================
INSERT IGNORE INTO skills_keywords (keyword, category) VALUES
-- Programming Languages
('java',          'Programming'),
('python',        'Programming'),
('c++',           'Programming'),
('javascript',    'Programming'),
('kotlin',        'Programming'),
('swift',         'Programming'),
('c#',            'Programming'),
('php',           'Programming'),
('ruby',          'Programming'),
('go',            'Programming'),
-- Web / Frameworks
('html',          'Web'),
('css',           'Web'),
('react',         'Web'),
('angular',       'Web'),
('spring',        'Web'),
('node.js',       'Web'),
('django',        'Web'),
('flask',         'Web'),
('bootstrap',     'Web'),
-- Databases
('mysql',         'Database'),
('postgresql',    'Database'),
('mongodb',       'Database'),
('oracle',        'Database'),
('sqlite',        'Database'),
('jdbc',          'Database'),
('hibernate',     'Database'),
-- Tools & DevOps
('git',           'Tools'),
('docker',        'Tools'),
('kubernetes',    'Tools'),
('jenkins',       'Tools'),
('maven',         'Tools'),
('gradle',        'Tools'),
('linux',         'Tools'),
('aws',           'Tools'),
('azure',         'Tools'),
-- Soft Skills
('communication', 'Soft Skills'),
('teamwork',      'Soft Skills'),
('leadership',    'Soft Skills'),
('problem solving','Soft Skills'),
('analytical',    'Soft Skills'),
('time management','Soft Skills');

-- ============================================================
-- Verify Setup
-- ============================================================
SELECT 'Database setup complete!' AS Status;
SELECT COUNT(*) AS total_keywords FROM skills_keywords;

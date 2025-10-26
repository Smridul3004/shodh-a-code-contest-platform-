package com.shodhai.dto;

import java.time.LocalDateTime;

public class LeaderboardEntryDto {
    private String username;
    private int problemsSolved;
    private int totalSubmissions;
    private LocalDateTime lastSubmissionTime;
    private int rank;
    
    // Constructors
    public LeaderboardEntryDto() {}
    
    public LeaderboardEntryDto(String username, int problemsSolved, int totalSubmissions, 
                              LocalDateTime lastSubmissionTime, int rank) {
        this.username = username;
        this.problemsSolved = problemsSolved;
        this.totalSubmissions = totalSubmissions;
        this.lastSubmissionTime = lastSubmissionTime;
        this.rank = rank;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getProblemsSolved() {
        return problemsSolved;
    }
    
    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }
    
    public int getTotalSubmissions() {
        return totalSubmissions;
    }
    
    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }
    
    public LocalDateTime getLastSubmissionTime() {
        return lastSubmissionTime;
    }
    
    public void setLastSubmissionTime(LocalDateTime lastSubmissionTime) {
        this.lastSubmissionTime = lastSubmissionTime;
    }
    
    public int getRank() {
        return rank;
    }
    
    public void setRank(int rank) {
        this.rank = rank;
    }
}

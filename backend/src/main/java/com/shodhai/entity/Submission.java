package com.shodhai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String submissionId;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;
    
    @Enumerated(EnumType.STRING)
    private SubmissionResult result;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column
    private Integer executionTime; // in milliseconds
    
    @Column
    private Integer memoryUsed; // in MB
    
    @Column(nullable = false)
    private LocalDateTime submittedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
    
    // Constructors
    public Submission() {}
    
    public Submission(String submissionId, String code, User user, Problem problem, Contest contest) {
        this.submissionId = submissionId;
        this.code = code;
        this.status = SubmissionStatus.PENDING;
        this.user = user;
        this.problem = problem;
        this.contest = contest;
        this.submittedAt = LocalDateTime.now();
    }
    
    // Enums
    public enum SubmissionStatus {
        PENDING, RUNNING, COMPLETED
    }
    
    public enum SubmissionResult {
        ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, MEMORY_LIMIT_EXCEEDED, RUNTIME_ERROR, COMPILATION_ERROR
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSubmissionId() {
        return submissionId;
    }
    
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public SubmissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
    
    public SubmissionResult getResult() {
        return result;
    }
    
    public void setResult(SubmissionResult result) {
        this.result = result;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Integer getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }
    
    public Integer getMemoryUsed() {
        return memoryUsed;
    }
    
    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Problem getProblem() {
        return problem;
    }
    
    public void setProblem(Problem problem) {
        this.problem = problem;
    }
    
    public Contest getContest() {
        return contest;
    }
    
    public void setContest(Contest contest) {
        this.contest = contest;
    }
}

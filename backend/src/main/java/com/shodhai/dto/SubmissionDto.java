package com.shodhai.dto;

import java.time.LocalDateTime;

public class SubmissionDto {
    private String submissionId;
    private String code;
    private String status;
    private String result;
    private String errorMessage;
    private Integer executionTime;
    private Integer memoryUsed;
    private LocalDateTime submittedAt;
    private String username;
    private String problemTitle;
    
    // Constructors
    public SubmissionDto() {}
    
    public SubmissionDto(String submissionId, String code, String status, String result, 
                        String errorMessage, Integer executionTime, Integer memoryUsed, 
                        LocalDateTime submittedAt, String username, String problemTitle) {
        this.submissionId = submissionId;
        this.code = code;
        this.status = status;
        this.result = result;
        this.errorMessage = errorMessage;
        this.executionTime = executionTime;
        this.memoryUsed = memoryUsed;
        this.submittedAt = submittedAt;
        this.username = username;
        this.problemTitle = problemTitle;
    }
    
    // Getters and Setters
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getProblemTitle() {
        return problemTitle;
    }
    
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }
}

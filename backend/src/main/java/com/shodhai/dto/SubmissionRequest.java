package com.shodhai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotNull(message = "Problem ID is required")
    private Long problemId;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    // Constructors
    public SubmissionRequest() {}
    
    public SubmissionRequest(String username, Long problemId, String code) {
        this.username = username;
        this.problemId = problemId;
        this.code = code;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getProblemId() {
        return problemId;
    }
    
    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}

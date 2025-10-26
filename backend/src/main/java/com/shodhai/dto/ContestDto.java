package com.shodhai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ContestDto {
    private Long id;
    private String contestId;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ProblemDto> problems;
    
    // Constructors
    public ContestDto() {}
    
    public ContestDto(Long id, String contestId, String name, String description, 
                     LocalDateTime startTime, LocalDateTime endTime, List<ProblemDto> problems) {
        this.id = id;
        this.contestId = contestId;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.problems = problems;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContestId() {
        return contestId;
    }
    
    public void setContestId(String contestId) {
        this.contestId = contestId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public List<ProblemDto> getProblems() {
        return problems;
    }
    
    public void setProblems(List<ProblemDto> problems) {
        this.problems = problems;
    }
}

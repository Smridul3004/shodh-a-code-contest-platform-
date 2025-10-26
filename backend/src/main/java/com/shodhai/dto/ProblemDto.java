package com.shodhai.dto;

public class ProblemDto {
    private Long id;
    private String title;
    private String description;
    private String sampleInput;
    private String sampleOutput;
    private Integer timeLimit;
    private Integer memoryLimit;
    
    // Constructors
    public ProblemDto() {}
    
    public ProblemDto(Long id, String title, String description, String sampleInput, 
                     String sampleOutput, Integer timeLimit, Integer memoryLimit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSampleInput() {
        return sampleInput;
    }
    
    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }
    
    public String getSampleOutput() {
        return sampleOutput;
    }
    
    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }
    
    public Integer getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public Integer getMemoryLimit() {
        return memoryLimit;
    }
    
    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
}

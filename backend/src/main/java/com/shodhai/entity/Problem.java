package com.shodhai.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String sampleInput;
    
    @Column(columnDefinition = "TEXT")
    private String sampleOutput;
    
    @Column(nullable = false)
    private Integer timeLimit; // in seconds
    
    @Column(nullable = false)
    private Integer memoryLimit; // in MB
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
    
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCases = new ArrayList<>();
    
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();
    
    // Constructors
    public Problem() {}
    
    public Problem(String title, String description, String sampleInput, String sampleOutput, 
                   Integer timeLimit, Integer memoryLimit, Contest contest) {
        this.title = title;
        this.description = description;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.contest = contest;
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
    
    public Contest getContest() {
        return contest;
    }
    
    public void setContest(Contest contest) {
        this.contest = contest;
    }
    
    public List<TestCase> getTestCases() {
        return testCases;
    }
    
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
    
    public List<Submission> getSubmissions() {
        return submissions;
    }
    
    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
}

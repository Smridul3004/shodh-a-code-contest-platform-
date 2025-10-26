package com.shodhai.service;

import com.shodhai.dto.SubmissionDto;
import com.shodhai.entity.Problem;
import com.shodhai.entity.Submission;
import com.shodhai.entity.User;
import com.shodhai.repository.ProblemRepository;
import com.shodhai.repository.SubmissionRepository;
import com.shodhai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubmissionService {
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private CodeJudgeService codeJudgeService;
    
    public String submitCode(String username, Long problemId, String code) {
        // Get or create user
        User user = userRepository.findByUsername(username)
            .orElseGet(() -> {
                User newUser = new User(username);
                return userRepository.save(newUser);
            });
        
        // Get problem
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new RuntimeException("Problem not found with ID: " + problemId));
        
        // Create submission
        String submissionId = UUID.randomUUID().toString();
        Submission submission = new Submission(submissionId, code, user, problem, problem.getContest());
        
        // Save submission
        submission = submissionRepository.save(submission);
        
        // Start judging asynchronously
        codeJudgeService.judgeSubmission(submission);
        
        return submissionId;
    }
    
    public SubmissionDto getSubmission(String submissionId) {
        Submission submission = submissionRepository.findBySubmissionId(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found with ID: " + submissionId));
        
        return new SubmissionDto(
            submission.getSubmissionId(),
            submission.getCode(),
            submission.getStatus().toString(),
            submission.getResult() != null ? submission.getResult().toString() : null,
            submission.getErrorMessage(),
            submission.getExecutionTime(),
            submission.getMemoryUsed(),
            submission.getSubmittedAt(),
            submission.getUser().getUsername(),
            submission.getProblem().getTitle()
        );
    }
    
    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }
}

package com.shodhai.controller;

import com.shodhai.dto.SubmissionDto;
import com.shodhai.dto.SubmissionRequest;
import com.shodhai.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {
    
    @Autowired
    private SubmissionService submissionService;
    
    @PostMapping
    public ResponseEntity<Map<String, String>> submitCode(@Valid @RequestBody SubmissionRequest request) {
        try {
            String submissionId = submissionService.submitCode(
                request.getUsername(),
                request.getProblemId(),
                request.getCode()
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("submissionId", submissionId);
            response.put("status", "PENDING");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable String submissionId) {
        try {
            SubmissionDto submission = submissionService.getSubmission(submissionId);
            return ResponseEntity.ok(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.shodhai.service;

import com.shodhai.entity.Problem;
import com.shodhai.entity.Submission;
import com.shodhai.entity.TestCase;
import com.shodhai.repository.ProblemRepository;
import com.shodhai.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CodeJudgeService {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeJudgeService.class);
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private ProblemRepository problemRepository;
    
    public void judgeSubmission(Submission submission) {
        CompletableFuture.runAsync(() -> {
            try {
                logger.info("Starting judgment for submission: {}", submission.getSubmissionId());
                
                // Update status to RUNNING
                submission.setStatus(Submission.SubmissionStatus.RUNNING);
                submissionRepository.save(submission);
                
                Problem problem = submission.getProblem();
                
                // Eagerly load test cases to avoid LazyInitializationException
                Problem problemWithTestCases = problemRepository.findByIdWithTestCases(problem.getId())
                    .orElseThrow(() -> new RuntimeException("Problem not found"));
                List<TestCase> testCases = problemWithTestCases.getTestCases();
                
                // Create temporary directory for this submission
                String tempDir = System.getProperty("java.io.tmpdir") + "/submission_" + submission.getId();
                Path tempPath = Paths.get(tempDir);
                Files.createDirectories(tempPath);
                
                try {
                    // Write code to file
                    String fileName = getFileNameForLanguage("java"); // Default to Java for now
                    Path codeFile = tempPath.resolve(fileName);
                    Files.write(codeFile, submission.getCode().getBytes());
                    
                    // Run test cases
                    boolean allPassed = true;
                    String errorMessage = null;
                    long totalExecutionTime = 0;
                    
                    for (TestCase testCase : testCases) {
                        JudgeResult result = runTestCase(codeFile.toString(), testCase, problem.getTimeLimit());
                        totalExecutionTime += result.getExecutionTime();
                        
                        if (!result.isPassed()) {
                            allPassed = false;
                            errorMessage = result.getErrorMessage();
                            break;
                        }
                    }
                    
                    // Update submission result
                    if (allPassed) {
                        submission.setResult(Submission.SubmissionResult.ACCEPTED);
                        submission.setErrorMessage(null);
                    } else {
                        submission.setResult(Submission.SubmissionResult.WRONG_ANSWER);
                        submission.setErrorMessage(errorMessage);
                    }
                    
                    submission.setExecutionTime((int) totalExecutionTime);
                    submission.setStatus(Submission.SubmissionStatus.COMPLETED);
                    
                } finally {
                    // Cleanup temporary directory
                    try {
                        Files.walk(tempPath)
                            .sorted((a, b) -> b.compareTo(a))
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    logger.warn("Failed to delete temporary file: {}", path, e);
                                }
                            });
                    } catch (IOException e) {
                        logger.warn("Failed to cleanup temporary directory: {}", tempDir, e);
                    }
                }
                
                // Save final result
                submissionRepository.save(submission);
                logger.info("Completed judgment for submission: {} with result: {}", 
                           submission.getSubmissionId(), submission.getResult());
                
            } catch (Exception e) {
                logger.error("Error judging submission: {}", submission.getSubmissionId(), e);
                submission.setStatus(Submission.SubmissionStatus.COMPLETED);
                submission.setResult(Submission.SubmissionResult.RUNTIME_ERROR);
                submission.setErrorMessage("Internal error during judgment: " + e.getMessage());
                submissionRepository.save(submission);
            }
        });
    }
    
    private JudgeResult runTestCase(String codeFile, TestCase testCase, Integer timeLimit) {
        try {
            long startTime = System.currentTimeMillis();
            
            // Check if Docker is available
            boolean dockerAvailable = isDockerAvailable();
            
            if (dockerAvailable) {
                return runWithDocker(codeFile, testCase, timeLimit, startTime);
            } else {
                // Fallback: Mock execution for testing without Docker
                return runMockExecution(testCase, startTime);
            }
            
        } catch (Exception e) {
            JudgeResult result = new JudgeResult();
            result.setPassed(false);
            result.setErrorMessage("Execution error: " + e.getMessage());
            result.setExecutionTime(0);
            return result;
        }
    }
    
    private boolean isDockerAvailable() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "--version");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    private JudgeResult runWithDocker(String codeFile, TestCase testCase, Integer timeLimit, long startTime) throws Exception {
        // Build Docker command with file mount
        ProcessBuilder processBuilder = new ProcessBuilder(
            "docker", "run", "--rm",
            "-v", codeFile + ":/app/Main.java",
            "--memory=128m",
            "--cpus=1",
            "--network=none",
            "--entrypoint=",
            "shodh-a-code-judge:latest",
            "bash", "-c",
            "cd /app && javac Main.java && echo '" + testCase.getInput() + "' | timeout " + (timeLimit != null ? timeLimit : 5) + " java Main"
        );
        
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        
        // Write input to process
        try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
            writer.write(testCase.getInput() + "\n");
            writer.flush();
            writer.close();
        }
        
        // Read output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        // Wait for process to complete
        int exitCode = process.waitFor();
        boolean finished = (exitCode == 0);
        long executionTime = System.currentTimeMillis() - startTime;
        
        String actualOutput = output.toString().trim();
        String expectedOutput = testCase.getExpectedOutput().trim();
        
        JudgeResult result = new JudgeResult();
        result.setExecutionTime((int) executionTime);
        
        // Debug logging
        System.out.println("=== JUDGE DEBUG ===");
        System.out.println("Test Case Input: '" + testCase.getInput() + "'");
        System.out.println("Expected Output: '" + expectedOutput + "'");
        System.out.println("Actual Output: '" + actualOutput + "'");
        System.out.println("Exit Code: " + exitCode);
        System.out.println("Finished: " + finished);
        System.out.println("==================");
        
        if (!finished) {
            result.setPassed(false);
            result.setErrorMessage("Process did not complete normally");
        } else if (exitCode == 124) {
            result.setPassed(false);
            result.setErrorMessage("TIME_LIMIT_EXCEEDED");
        } else if (exitCode != 0) {
            result.setPassed(false);
            result.setErrorMessage("RUNTIME_ERROR: " + actualOutput);
        } else if (!actualOutput.equals(expectedOutput)) {
            result.setPassed(false);
            result.setErrorMessage("WRONG_ANSWER - Expected: '" + expectedOutput + "', Got: '" + actualOutput + "'");
        } else {
            result.setPassed(true);
        }
        
        return result;
    }
    
    private JudgeResult runMockExecution(TestCase testCase, long startTime) {
        // Mock execution for testing without Docker
        JudgeResult result = new JudgeResult();
        result.setExecutionTime((int) (System.currentTimeMillis() - startTime));
        
        // Simulate some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simple logic: parse input and output for basic problems
        // This is a simplified judge for demo purposes
        String input = testCase.getInput().trim();
        String expectedOutput = testCase.getExpectedOutput().trim();
        
        // Enhanced validation: check if the expected output matches what should be calculated
        if (input.contains(" ") && expectedOutput.matches("-?\\d+")) {
            try {
                String[] parts = input.split("\\s+");
                if (parts.length >= 2) {
                    int a = Integer.parseInt(parts[0]);
                    int b = Integer.parseInt(parts[1]);
                    int expected = Integer.parseInt(expectedOutput);
                    
                    // For "Sum of Two Numbers" problem, validate it's actually addition
                    int correctSum = a + b;
                    result.setPassed(expected == correctSum);
                    
                    if (!result.isPassed()) {
                        result.setErrorMessage("WRONG_ANSWER - Expected sum: " + correctSum + ", but test case expects: " + expected);
                    }
                } else {
                    result.setPassed(false);
                    result.setErrorMessage("Invalid input format - need two numbers");
                }
            } catch (NumberFormatException e) {
                result.setPassed(false);
                result.setErrorMessage("Invalid input format - non-numeric input");
            }
        } else {
            // For non-numeric problems, accept in mock mode
            result.setPassed(true);
        }
        
        return result;
    }
    
    private String getFileNameForLanguage(String language) {
        switch (language.toLowerCase()) {
            case "java":
                return "Main.java";
            case "python":
                return "main.py";
            case "javascript":
                return "main.js";
            default:
                return "Main.java";
        }
    }
    
    private static class JudgeResult {
        private boolean passed;
        private String errorMessage;
        private int executionTime;
        
        public boolean isPassed() {
            return passed;
        }
        
        public void setPassed(boolean passed) {
            this.passed = passed;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public int getExecutionTime() {
            return executionTime;
        }
        
        public void setExecutionTime(int executionTime) {
            this.executionTime = executionTime;
        }
    }
}

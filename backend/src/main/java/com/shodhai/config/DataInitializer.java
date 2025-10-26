package com.shodhai.config;

import com.shodhai.entity.*;
import com.shodhai.repository.ContestRepository;
import com.shodhai.repository.ProblemRepository;
import com.shodhai.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ContestRepository contestRepository;
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private TestCaseRepository testCaseRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample contest
        Contest contest = new Contest(
            "CONTEST001",
            "Shodh AI Coding Challenge",
            "Welcome to the first Shodh AI coding contest! Solve these problems to test your skills.",
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().plusHours(23)
        );
        contest = contestRepository.save(contest);
        
        // Create Problem 1: Sum of Two Numbers
        Problem problem1 = new Problem(
            "Sum of Two Numbers",
            "Write a program that takes two integers as input and outputs their sum.\n\n" +
            "Input: Two integers separated by a space\n" +
            "Output: The sum of the two integers",
            "5 3",
            "8",
            2, // 2 seconds time limit
            128, // 128 MB memory limit
            contest
        );
        problem1 = problemRepository.save(problem1);
        
        // Add test cases for Problem 1
        TestCase testCase1_1 = new TestCase("5 3", "8", problem1);
        TestCase testCase1_2 = new TestCase("10 20", "30", problem1);
        TestCase testCase1_3 = new TestCase("-5 5", "0", problem1);
        TestCase testCase1_4 = new TestCase("0 0", "0", problem1);
        
        testCaseRepository.saveAll(Arrays.asList(testCase1_1, testCase1_2, testCase1_3, testCase1_4));
        
        // Create Problem 2: Factorial
        Problem problem2 = new Problem(
            "Factorial",
            "Write a program that calculates the factorial of a given number.\n\n" +
            "Input: A single integer n (0 ≤ n ≤ 10)\n" +
            "Output: The factorial of n",
            "5",
            "120",
            3, // 3 seconds time limit
            128, // 128 MB memory limit
            contest
        );
        problem2 = problemRepository.save(problem2);
        
        // Add test cases for Problem 2
        TestCase testCase2_1 = new TestCase("5", "120", problem2);
        TestCase testCase2_2 = new TestCase("3", "6", problem2);
        TestCase testCase2_3 = new TestCase("0", "1", problem2);
        TestCase testCase2_4 = new TestCase("1", "1", problem2);
        
        testCaseRepository.saveAll(Arrays.asList(testCase2_1, testCase2_2, testCase2_3, testCase2_4));
        
        // Create Problem 3: Fibonacci Sequence
        Problem problem3 = new Problem(
            "Fibonacci Sequence",
            "Write a program that prints the nth Fibonacci number.\n\n" +
            "Input: A single integer n (1 ≤ n ≤ 20)\n" +
            "Output: The nth Fibonacci number",
            "7",
            "13",
            2, // 2 seconds time limit
            128, // 128 MB memory limit
            contest
        );
        problem3 = problemRepository.save(problem3);
        
        // Add test cases for Problem 3
        TestCase testCase3_1 = new TestCase("7", "13", problem3);
        TestCase testCase3_2 = new TestCase("1", "1", problem3);
        TestCase testCase3_3 = new TestCase("2", "1", problem3);
        TestCase testCase3_4 = new TestCase("10", "55", problem3);
        
        testCaseRepository.saveAll(Arrays.asList(testCase3_1, testCase3_2, testCase3_3, testCase3_4));
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Contest ID: CONTEST001");
        System.out.println("Problems created: 3");
    }
}

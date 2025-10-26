package com.shodhai.service;

import com.shodhai.dto.ContestDto;
import com.shodhai.dto.LeaderboardEntryDto;
import com.shodhai.dto.ProblemDto;
import com.shodhai.entity.Contest;
import com.shodhai.entity.Problem;
import com.shodhai.entity.Submission;
import com.shodhai.repository.ContestRepository;
import com.shodhai.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContestService {
    
    @Autowired
    private ContestRepository contestRepository;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    public ContestDto getContestByContestId(String contestId) {
        Contest contest = contestRepository.findByContestId(contestId)
            .orElseThrow(() -> new RuntimeException("Contest not found with ID: " + contestId));
        
        List<ProblemDto> problemDtos = contest.getProblems().stream()
            .map(this::convertToProblemDto)
            .collect(Collectors.toList());
        
        return new ContestDto(
            contest.getId(),
            contest.getContestId(),
            contest.getName(),
            contest.getDescription(),
            contest.getStartTime(),
            contest.getEndTime(),
            problemDtos
        );
    }
    
    public List<LeaderboardEntryDto> getLeaderboard(String contestId) {
        Contest contest = contestRepository.findByContestId(contestId)
            .orElseThrow(() -> new RuntimeException("Contest not found with ID: " + contestId));
        
        List<Submission> acceptedSubmissions = submissionRepository.findAcceptedSubmissionsByContest(contest.getId());
        
        // Group by user and count unique problems solved
        Map<String, List<Submission>> userSubmissions = acceptedSubmissions.stream()
            .collect(Collectors.groupingBy(submission -> submission.getUser().getUsername()));
        
        List<LeaderboardEntryDto> leaderboard = userSubmissions.entrySet().stream()
            .map(entry -> {
                String username = entry.getKey();
                List<Submission> submissions = entry.getValue();
                
                // Count unique problems solved
                long problemsSolved = submissions.stream()
                    .map(submission -> submission.getProblem().getId())
                    .distinct()
                    .count();
                
                // Get total submissions for this user
                long totalSubmissions = submissionRepository.findByContestId(contestId).stream()
                    .filter(sub -> sub.getUser().getUsername().equals(username))
                    .count();
                
                // Get last submission time
                submissions.stream()
                    .max((s1, s2) -> s1.getSubmittedAt().compareTo(s2.getSubmittedAt()))
                    .ifPresent(lastSubmission -> {
                        // This will be handled in the mapping
                    });
                
                return new LeaderboardEntryDto(
                    username,
                    (int) problemsSolved,
                    (int) totalSubmissions,
                    submissions.stream()
                        .max((s1, s2) -> s1.getSubmittedAt().compareTo(s2.getSubmittedAt()))
                        .map(Submission::getSubmittedAt)
                        .orElse(null),
                    0 // Rank will be set after sorting
                );
            })
            .sorted((a, b) -> {
                // Sort by problems solved (desc), then by last submission time (asc)
                int problemsComparison = Integer.compare(b.getProblemsSolved(), a.getProblemsSolved());
                if (problemsComparison != 0) return problemsComparison;
                
                if (a.getLastSubmissionTime() == null) return 1;
                if (b.getLastSubmissionTime() == null) return -1;
                return a.getLastSubmissionTime().compareTo(b.getLastSubmissionTime());
            })
            .collect(Collectors.toList());
        
        // Set ranks
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank(i + 1);
        }
        
        return leaderboard;
    }
    
    private ProblemDto convertToProblemDto(Problem problem) {
        return new ProblemDto(
            problem.getId(),
            problem.getTitle(),
            problem.getDescription(),
            problem.getSampleInput(),
            problem.getSampleOutput(),
            problem.getTimeLimit(),
            problem.getMemoryLimit()
        );
    }
}

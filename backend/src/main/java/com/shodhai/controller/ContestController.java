package com.shodhai.controller;

import com.shodhai.dto.ContestDto;
import com.shodhai.dto.LeaderboardEntryDto;
import com.shodhai.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = "*")
public class ContestController {
    
    @Autowired
    private ContestService contestService;
    
    @GetMapping("/{contestId}")
    public ResponseEntity<ContestDto> getContest(@PathVariable String contestId) {
        try {
            ContestDto contest = contestService.getContestByContestId(contestId);
            return ResponseEntity.ok(contest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(@PathVariable String contestId) {
        try {
            List<LeaderboardEntryDto> leaderboard = contestService.getLeaderboard(contestId);
            return ResponseEntity.ok(leaderboard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

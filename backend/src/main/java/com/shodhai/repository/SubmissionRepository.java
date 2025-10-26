package com.shodhai.repository;

import com.shodhai.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findBySubmissionId(String submissionId);
    
    @Query("SELECT s FROM Submission s WHERE s.contest.contestId = :contestId ORDER BY s.submittedAt DESC")
    List<Submission> findByContestIdOrderBySubmittedAtDesc(@Param("contestId") String contestId);
    
    @Query("SELECT s FROM Submission s WHERE s.contest.contestId = :contestId ORDER BY s.submittedAt DESC")
    List<Submission> findByContestId(@Param("contestId") String contestId);
    
    @Query("SELECT s FROM Submission s WHERE s.contest.id = :contestId AND s.result = 'ACCEPTED' " +
           "ORDER BY s.submittedAt ASC")
    List<Submission> findAcceptedSubmissionsByContest(@Param("contestId") Long contestId);
}

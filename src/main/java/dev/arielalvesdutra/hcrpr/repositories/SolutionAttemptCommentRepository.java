package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;

public interface SolutionAttemptCommentRepository extends 
	JpaRepository<SolutionAttemptComment, Long> {

	Page<SolutionAttemptComment> findBySolutionAttempt_Id(Long id, Pageable pageable);
}

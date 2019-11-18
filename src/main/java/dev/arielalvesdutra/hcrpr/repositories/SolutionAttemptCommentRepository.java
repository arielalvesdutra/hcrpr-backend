package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;

public interface SolutionAttemptCommentRepository extends 
	JpaRepository<SolutionAttemptComment, Long> {
	
}

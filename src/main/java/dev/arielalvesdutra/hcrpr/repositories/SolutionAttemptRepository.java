package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;

public interface SolutionAttemptRepository extends JpaRepository<SolutionAttempt, Long>{
	SolutionAttempt findByIdAndProblem_Id(Long id, Long problemId);
}

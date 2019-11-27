package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.ProblemComment;


public interface ProblemCommentRepository extends JpaRepository<ProblemComment, Long> {

	Page<ProblemComment> findAllByProblem_Id(Long id, Pageable pageable);
}

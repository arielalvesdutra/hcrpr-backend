package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.ProblemComment;


public interface ProblemCommentRepository extends JpaRepository<ProblemComment, Long> {

}

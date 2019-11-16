package dev.arielalvesdutra.hcrpr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;

@Service
public class ProblemService {
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private ProblemCommentRepository problemCommentRepository;

	public Problem create(Problem problem) {
		return this.problemRepository.save(problem);
	}

	public List<Problem> findAll() {
		return this.problemRepository.findAll();
	}

	public Page<Problem> findAll(Pageable pageable) {
		return this.problemRepository.findAll(pageable);
	}

	public Problem findById(Long id) {
		return this.problemRepository
				.findById(id)
				.orElseThrow(() -> 
				new EntityNotFoundException("Problema com ID "+ id +" não encontrado"));
	}

	public void deleteById(Long id) {
		Problem problem = this.findById(id);
		
		this.problemRepository.delete(problem);	
	}

	public Problem update(Long id, Problem parameterProblem) {

		Problem existingProblem = this.findById(id);
		
		existingProblem.setName(parameterProblem.getName());
		existingProblem.setDescription(parameterProblem.getDescription());
		this.problemRepository.save(existingProblem);
		
		return existingProblem;
	}

	@Transactional
	public Set<Concept> updateProblemRelatedConcepts(Long problemId, Set<Concept> concepts) {
		Problem existingProblem = this.findById(problemId);
		
		existingProblem.setRelatedConcepts(concepts);
		
		return existingProblem.getRelatedConcepts();
	}

	public Page<Concept> findAllProblemRelatedConcepts(Long problemId, Pageable pageable) {
		Problem problem = this.findById(problemId);

		List<Concept> problemRelatedConceptsAsList = 
				new ArrayList<Concept>(problem.getRelatedConcepts());
		
		
		return new PageImpl<Concept>(problemRelatedConceptsAsList, pageable, pageable.getPageSize());
	}

	@Transactional
	public ProblemComment createProblemComment(Long problemId, ProblemComment problemComment) {

		Problem problem = this.findById(problemId);
		problem.getComments().add(problemComment);
		problemComment.setProblem(problem);
		
		return problemComment;
	}

	public Page<ProblemComment> findAllProblemComments(Long problemId, Pageable pageable) {
		Problem problem = this.findById(problemId);

		List<ProblemComment> problemCommentsAsList = 
				new ArrayList<ProblemComment>(problem.getComments());
		
		return new PageImpl<ProblemComment>(problemCommentsAsList, pageable, pageable.getPageSize());
	}
	
	@Transactional
	public void deleteProblemComment(Long problemId, Long problemCommentId) {

		Problem problem = this.findById(problemId);
		ProblemComment comment = this.problemCommentRepository.findById(problemCommentId).get();
		
		problem.removeComment(comment);
	}

	@Transactional
	public Goal createProblemGoal(Long problemId, Goal goal) {
		Problem problem = this.findById(problemId);
		
		problem.getGoals().add(goal);
		goal.setProblem(problem);
		
		return goal;
	}

	public Page<Goal> findAllProblemGoals(Long problemId, Pageable pageable) {	
		Problem problem = this.findById(problemId);
	
		List<Goal> problemGoalsAsList = new ArrayList<Goal>(problem.getGoals());
		
		return new PageImpl<Goal>(problemGoalsAsList, pageable, pageable.getPageSize());
	}
}

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
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.GoalRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptRepository;

@Service
public class ProblemService {
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private ProblemCommentRepository problemCommentRepository;
	
	@Autowired
	private GoalRepository goalRepository;
	
	@Autowired
	private SolutionAttemptRepository solutionAttemptRepository;
	
	@Autowired
	private SolutionAttemptCommentRepository solutionAttemptCommentRepository;

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
	public Set<Concept> updateRelatedConcepts(Long problemId, Set<Concept> concepts) {
		Problem existingProblem = this.findById(problemId);
		
		existingProblem.setRelatedConcepts(concepts);
		
		return existingProblem.getRelatedConcepts();
	}

	public Page<Concept> findAllRelatedConcepts(Long problemId, Pageable pageable) {
		Problem problem = this.findById(problemId);

		List<Concept> problemRelatedConceptsAsList = 
				new ArrayList<Concept>(problem.getRelatedConcepts());
		
		
		return new PageImpl<Concept>(problemRelatedConceptsAsList, pageable, pageable.getPageSize());
	}

	@Transactional
	public ProblemComment createProblemComment(Long problemId, ProblemComment problemComment) {

		Problem problem = this.findById(problemId);
		
		problem.addComment(problemComment);
		
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
	public Goal createGoal(Long problemId, Goal goal) {
		Problem problem = this.findById(problemId);
		
		problem.addGoal(goal);
		
		return goal;
	}

	public Page<Goal> findAllGoals(Long problemId, Pageable pageable) {	
		Problem problem = this.findById(problemId);
	
		List<Goal> problemGoalsAsList = new ArrayList<Goal>(problem.getGoals());
		
		return new PageImpl<Goal>(problemGoalsAsList, pageable, pageable.getPageSize());
	}

	@Transactional
	public Goal updateGoal(Long problemId, Long goalId, Goal parameterGoal) {
		Problem problem = this.findById(problemId);
		Goal goal = this.goalRepository.findById(goalId).get();
		
		if (! problem.getGoals().contains(goal)) {
			throw new EntityNotFoundException("O objetivo de ID "
					+ goal.getId()
					+" não pertence ao problema de ID " + problem.getId());
		}
		
		goal.setAchieved(parameterGoal.getAchieved());
		goal.setDescription(parameterGoal.getDescription());
		
		return goal;
	}

	@Transactional
	public void deleteGoal(Long problemId, Long goalId) {
		Problem problem = this.findById(problemId);
		Goal goal = this.goalRepository.findById(goalId).get();
		
		problem.removeGoal(goal);
	}

	@Transactional
	public SolutionAttempt createSolutionAttempt(
			Long problemId, SolutionAttempt solutionAttempt) {
		
		Problem problem = this.findById(problemId);
		
		problem.addSolutionAttempt(solutionAttempt);
		
		return solutionAttempt;		
	}

	public Page<SolutionAttempt> findAllSolutionAttempts(
			Long problemId, Pageable pageable) {
		
		Problem problem = this.findById(problemId);
		
		List<SolutionAttempt> problemSolutionAttemptsAsList = 
				new ArrayList<SolutionAttempt>(problem.getSolutionAttempts());
		
		return new PageImpl<SolutionAttempt>(problemSolutionAttemptsAsList, pageable, pageable.getPageSize());
	}

	public SolutionAttempt findSolutionAttempt(Long problemId, Long solutionAttemptId) {

		SolutionAttempt fetchedSolutionAttempt = 
				this.solutionAttemptRepository.findByIdAndProblem_Id(solutionAttemptId, problemId);

		if (fetchedSolutionAttempt == null) {
			throw new EntityNotFoundException("A tentativa de ID "
					+ solutionAttemptId
					+" não existe ou não pertence ao problema de ID " + problemId);
		}
		
		return fetchedSolutionAttempt;
	}

	@Transactional
	public SolutionAttempt updateSolutionAttempt(
			Long problemId, Long solutionAttemptId, SolutionAttempt parameterSolutionAttempt) {
		SolutionAttempt existingSolutionAttempt = 
				this.findSolutionAttempt(problemId, solutionAttemptId);
		
		existingSolutionAttempt.setDescription(parameterSolutionAttempt.getDescription());
		existingSolutionAttempt.setName(parameterSolutionAttempt.getName());
		
		
		return existingSolutionAttempt;
	}

	@Transactional
	public void deleteSolutionAttempt(Long problemId, Long solutionAttemptId) {

		Problem problem = this.findById(problemId);
		SolutionAttempt solutionAttempt = 
				this.solutionAttemptRepository.findByIdAndProblem_Id(solutionAttemptId, problemId);
		
		problem.removeSolutionAttempt(solutionAttempt);		
	}

	@Transactional
	public Set<Technique> updateSolutionAttemptTechniques(
			Long problemId, Long solutionAttemptId, Set<Technique> techniques) {
		
		SolutionAttempt solutionAttempt = this.findSolutionAttempt(problemId, solutionAttemptId);
		
		solutionAttempt.setTechniques(techniques);
		
		return techniques;
	}

	public Page<Technique> findAllSolutionAttemptTechniques(
			Long problemId, Long solutionAttemptId, Pageable pageable) {

		
		SolutionAttempt solutionAttempt = this.findSolutionAttempt(problemId, solutionAttemptId);
		
		List<Technique> techniquesAsList = 
				new ArrayList<Technique>(solutionAttempt.getTechniques());
		
		return new PageImpl<Technique>(techniquesAsList, pageable, pageable.getPageSize());
	}

	@Transactional
	public SolutionAttemptComment createSolutionAttemptComment(
			Long problemId, Long solutionAttemptId, SolutionAttemptComment attemptComment) {

		SolutionAttempt solutionAttempt = this.findSolutionAttempt(problemId, solutionAttemptId);
		
		solutionAttempt.addComment(attemptComment);
		
		return attemptComment;
	}

	public Page<SolutionAttemptComment> findAllSolutionAttemptComments(
			Long problemId, Long solutionAttemptId, Pageable pageable) {
		
		SolutionAttempt solutionAttempt = this.findSolutionAttempt(problemId, solutionAttemptId);
		
		List<SolutionAttemptComment> commentsAsList = 
				new ArrayList<SolutionAttemptComment>(solutionAttempt.getComments());
		
		return new PageImpl<SolutionAttemptComment>(commentsAsList, pageable, pageable.getPageSize());
	}

	@Transactional
	public void deleteSolutionAttemptComment(Long problemId, Long solutionAttemptId, Long commentId) {		

		SolutionAttempt solutionAttempt = 
				this.solutionAttemptRepository.findByIdAndProblem_Id(solutionAttemptId, problemId);
		SolutionAttemptComment comment = 
				this.solutionAttemptCommentRepository.findById(commentId).get();
		
		solutionAttempt.removeComment(comment);
	}
}

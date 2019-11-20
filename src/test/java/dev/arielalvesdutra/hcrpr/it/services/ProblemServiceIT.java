package dev.arielalvesdutra.hcrpr.it.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.builders.ConceptBuilder;
import dev.arielalvesdutra.hcrpr.builders.ProblemBuilder;
import dev.arielalvesdutra.hcrpr.builders.SolutionAttemptBuilder;
import dev.arielalvesdutra.hcrpr.builders.TechniqueBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.repositories.GoalRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptRepository;
import dev.arielalvesdutra.hcrpr.repositories.TechniqueRepository;
import dev.arielalvesdutra.hcrpr.services.ConceptService;
import dev.arielalvesdutra.hcrpr.services.ProblemService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
@ActiveProfiles("it")
public class ProblemServiceIT {
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private GoalRepository goalRepository;
	
	@Autowired
	private ProblemCommentRepository problemCommentRepository;
	
	@Autowired
	private SolutionAttemptCommentRepository solutionAttemptCommentRepository;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private ConceptRepository conceptRepository;
	
	@Autowired
	private TechniqueRepository techniqueRepository;
	
	@Autowired
	private SolutionAttemptRepository solutionAttemptRepository;
	
	@After
	public void tearDown() {
		this.problemRepository.deleteAll();
		this.conceptRepository.deleteAll();
		this.problemCommentRepository.deleteAll();
		this.goalRepository.deleteAll();
		this.solutionAttemptRepository.deleteAll();
		this.techniqueRepository.deleteAll();
		this.solutionAttemptCommentRepository.deleteAll();
	}
	
	@Test
	public void createProblem_withAllRelations_shouldWork() {
		Set<Concept> concepts = new HashSet<Concept>();
		Concept concept = this.buildAndSaveASimpleConcept();
		concepts.add(concept);
		Problem problem = this.buildASimpleProblem();
		ProblemComment problemComment = this.buildASimpleProblemCommentWithAProblem(problem);
		SolutionAttempt solutionAttempt = this.buildASimpleSulutionAttemptWithAProblem(problem);
		Goal goal = this.buildASimpleGoalWithAProblem(problem);
		
		problem.addGoal(goal);
		problem.addSolutionAttempt(solutionAttempt);
		problem.addComment(problemComment);
		problem.setRelatedConcepts(concepts);
		
		
		Problem createdProblem = this.problemService.create(problem);
		Problem updatedProblem = 
				this.problemRepository.findById(createdProblem.getId()).get();
		
		
		assertThat(updatedProblem).isNotNull();
		assertThat(updatedProblem.getGoals()).size().isEqualTo(1);
		assertThat(updatedProblem.getComments()).size().isEqualTo(1);
		assertThat(updatedProblem.getSolutionAttempts()).size().isEqualTo(1);
		assertThat(updatedProblem.getRelatedConcepts()).contains(concept);
	}

	@Test
	public void createProblem_shouldWork() {
		Problem problem = this.buildASimpleProblem();
		
		Problem createdProblem = this.problemService.create(problem);
		
		assertThat(createdProblem).isNotNull();
		assertThat(createdProblem.getName()).isEqualTo(problem.getName());
		assertThat(createdProblem.getDescription()).isEqualTo(problem.getDescription());
		assertThat(createdProblem.getId()).isNotNull();
		assertThat(createdProblem.getCreatedAt())
				.describedAs("CreatedAt não pode ser nulo").isNotNull();
	}
	
	@Test
	public void findAll_shouldWork() {
		Problem problem = this.buildAndSaveASimpleProblem();	
		
		List<Problem> fetchedProblems = this.problemService.findAll();
		
		assertThat(fetchedProblems).isNotNull();
		assertThat(fetchedProblems.contains(problem)).isTrue();
	}
	
	@Test
	public void findAll_withPageable_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<Problem> fetchedProblemPage = this.problemService.findAll(pageable);
		
		assertThat(fetchedProblemPage).isNotNull();
		assertThat(fetchedProblemPage.getContent().contains(createdProblem)).isTrue();
	}
	
	@Test
	public void findById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();	
		
		Problem fetchedProblem = 
					this.problemService.findById(createdProblem.getId());
		
		assertThat(fetchedProblem).isNotNull();
		assertThat(fetchedProblem.getId()).isEqualTo(createdProblem.getId());
		assertThat(fetchedProblem.getName()).isEqualTo(createdProblem.getName());
		assertThat(fetchedProblem.getDescription()).isEqualTo(createdProblem.getDescription());
		assertThat(fetchedProblem.getCreatedAt()).isEqualTo(createdProblem.getCreatedAt());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void findById_withoutProblems_shouldThrownAnEntityNotFoundException() {
		Long id = 1L;
		this.problemService.findById(id);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();	
		
		this.problemService.deleteById(createdProblem.getId());
		Optional<Problem> fetchedConcept = 
					this.problemRepository.findById(createdProblem.getId());
		
		assertThat(fetchedConcept.isPresent()).isFalse();
	}
	
	
	@Test
	public void updateProblem_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();		
		
		
		Problem problemToUpdate = new ProblemBuilder()
				.withName("Problema modificado")
				.withDescription("Descrição modificada")
				.build();
		Problem updatedConcept =
				this.problemService.update(createdProblem.getId(), problemToUpdate);
		
		
		assertThat(updatedConcept).isNotNull();
		assertThat(updatedConcept.getId()).isEqualTo(createdProblem.getId());
		assertThat(updatedConcept.getName()).isEqualTo(problemToUpdate.getName());
		assertThat(updatedConcept.getDescription()).isEqualTo(problemToUpdate.getDescription());
		assertThat(updatedConcept.getCreatedAt()).isEqualTo(createdProblem.getCreatedAt());
	}
	
	@Test
	public void updateProblemRelatedConcepts_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		Set<Concept> concepts = new HashSet<Concept>(); 
		concepts.add(createdConcept);
		
		
		this.problemService.updateRelatedConcepts(createdProblem.getId(), concepts);
		Problem updatedProblem = this.problemService.findById(createdProblem.getId());
		
		
		assertThat(updatedProblem.getRelatedConcepts()).isNotNull();
		assertThat(updatedProblem.getRelatedConcepts()).contains(createdConcept);
	}
	
	@Test
	public void findAllProblemRelatedConcepts_withPageable_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		Set<Concept> concepts = new HashSet<Concept>(); 
		concepts.add(createdConcept);
		
		Problem problem = this.buildASimpleProblem();
		problem.setRelatedConcepts(concepts);
		Problem createdProblem = this.problemRepository.save(problem);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<Concept> problemRelatedConceptsPage = 
					this.problemService.findAllRelatedConcepts(createdProblem.getId(), pageable);
		
		
		assertThat(problemRelatedConceptsPage).isNotNull();
		assertThat(problemRelatedConceptsPage.getContent()).contains(createdConcept);
	}
	
	@Test
	public void createProblemComment_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		ProblemComment problemComment = new ProblemComment("Primeiro comentário para o problema");
		
		ProblemComment createdProblemComment = 
				this.problemService.createProblemComment(createdProblem.getId(), problemComment);
		Problem updatedProblem = this.problemRepository.findById(createdProblem.getId()).get();
		
		
		assertThat(createdProblemComment).isNotNull();
		assertThat(createdProblemComment.getId()).isNotNull();
		assertThat(createdProblemComment.getContent()).isEqualTo(problemComment.getContent());
		assertThat(updatedProblem.getComments()).contains(createdProblemComment);
	}
	
	@Test
	public void findAllProblemComments_withPageable_shouldWork() {		
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		ProblemComment problemComment = new ProblemComment("Comentário para o problema");
		problemComment.setProblem(createdProblem); 
		ProblemComment createdComment = this.problemCommentRepository.save(problemComment); 
		Pageable pageable = PageRequest.of(0, 10, Sort.by("content"));
		
		Page<ProblemComment> fetchedProblemComments = 
				this.problemService.findAllProblemComments(createdProblem.getId(), pageable);
		
		
		assertThat(fetchedProblemComments).isNotNull();
		assertThat(fetchedProblemComments.getContent()).contains(createdComment);
	}
	
	@Test
	public void deleteProblemCommentByCommentId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		ProblemComment problemComment = new ProblemComment("Comentário para o problema");
		problemComment.setProblem(createdProblem); 
		ProblemComment createdComment = this.problemCommentRepository.save(problemComment);
	
		this.problemService.deleteProblemComment(
				createdProblem.getId(), createdComment.getId());
		Optional<ProblemComment> fetchedComment = 
				this.problemCommentRepository.findById(createdComment.getId());
		
		
		assertThat(fetchedComment.isPresent()).isFalse();		
	}
	
	@Test
	public void createProblemGoal_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal goal = new Goal("Resolver questão X");
		
		Goal createdGoal = this.problemService.createGoal(createdProblem.getId(), goal);
		Problem updatedProblem = this.problemRepository.findById(createdProblem.getId()).get();
		
		assertThat(createdGoal).isNotNull();
		assertThat(createdGoal.getId()).isNotNull();
		assertThat(createdGoal.getDescription()).isEqualTo(goal.getDescription());		
		assertThat(updatedProblem.getGoals()).contains(createdGoal);
	}
	
	@Test
	public void findAllProblemGoals_withPageable_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveASimpleGoalWithAProblem(createdProblem);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("content"));
		
		Page<Goal> goalsPage = 
				this.problemService.findAllGoals(createdProblem.getId(), pageable);
		
		
		assertThat(goalsPage).isNotNull();
		assertThat(goalsPage.getContent()).isNotNull();
		assertThat(goalsPage).contains(createdGoal);
	}
	
	@Test
	public void updateProblemGoalByGoalId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveASimpleGoalWithAProblem(createdProblem);
		Goal goalToUpdate = new Goal("Resolver questão X e Y");
		goalToUpdate.setAchieved(true);
		
		
		this.problemService.updateGoal(
				createdProblem.getId(), createdGoal.getId(), goalToUpdate);
		Goal updatedGoal = this.goalRepository.findById(createdGoal.getId()).get();
		
		
		assertThat(updatedGoal).isNotNull();
		assertThat(updatedGoal.getDescription()).isEqualTo(goalToUpdate.getDescription());
		assertThat(updatedGoal.getAchieved()).isEqualTo(goalToUpdate.getAchieved());
		assertThat(updatedGoal.getCreatedAt()).isEqualTo(createdGoal.getCreatedAt());
		assertThat(updatedGoal.getId()).isEqualTo(createdGoal.getId());
	}
	
	@Test
	public void deleteProblemGoalByGoalId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveASimpleGoalWithAProblem(createdProblem);
		
		this.problemService.deleteGoal(createdProblem.getId(), createdGoal.getId());
		Optional<Goal> fetchedGoal = 
				this.goalRepository.findById(createdGoal.getId());
		
		
		assertThat(fetchedGoal.isPresent()).isFalse();	
	}
	
	@Test
	public void createProblemSolutionAttempt_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt solutionAttempt = this.buildASimpleSulutionAttempt();
		
		SolutionAttempt createdSolutionAttempt =
				this.problemService.createSolutionAttempt(createdProblem.getId(), solutionAttempt);
		Problem updatedProblem = this.problemRepository.findById(createdProblem.getId()).get();
		
		
		assertThat(createdSolutionAttempt).isNotNull();
		assertThat(createdSolutionAttempt.getId()).isNotNull();
		assertThat(createdSolutionAttempt.getName()).isEqualTo(solutionAttempt.getName());
		assertThat(createdSolutionAttempt.getDescription()).isEqualTo(solutionAttempt.getDescription());
		assertThat(updatedProblem.getSolutionAttempts()).contains(createdSolutionAttempt);
	}
	
	@Test
	public void findAllProblemSolutionAttempts_withPageable_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<SolutionAttempt> solutionAttemptsPage = 
				this.problemService.findAllSolutionAttempts(createdProblem.getId(), pageable);
		
		assertThat(solutionAttemptsPage).isNotNull();
		assertThat(solutionAttemptsPage.getContent()).isNotNull();
		assertThat(solutionAttemptsPage.getContent()).contains(createdSolutionAttempt);
	}
	
	@Test
	public void findProblemSolutionAttempt_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		
		SolutionAttempt fetchedAttempt = this.problemService.findSolutionAttempt(
				createdProblem.getId(), createdSolutionAttempt.getId());
		
		assertThat(fetchedAttempt).isNotNull();
		assertThat(fetchedAttempt.getId()).isEqualTo(createdSolutionAttempt.getId());
		assertThat(fetchedAttempt.getName()).isEqualTo(createdSolutionAttempt.getName());
		assertThat(fetchedAttempt.getDescription()).isEqualTo(createdSolutionAttempt.getDescription());
		assertThat(fetchedAttempt.getProblem()).isEqualTo(createdProblem);
	}
	
	@Test
	public void updateProblemSolutionAttemptByAttemptId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		SolutionAttempt solutionAttemptToUpdate = new SolutionAttempt();
		solutionAttemptToUpdate.setName("Nome da tentiva modificado");
		solutionAttemptToUpdate.setDescription("Descrição modificada");
		
		this.problemService.updateSolutionAttempt(
				createdProblem.getId(), createdSolutionAttempt.getId(), solutionAttemptToUpdate);
		SolutionAttempt updatedAttempt = 
				this.solutionAttemptRepository.findById(createdSolutionAttempt.getId()).get();
		
		assertThat(updatedAttempt).isNotNull();
		assertThat(updatedAttempt.getId()).isEqualTo(createdSolutionAttempt.getId());
		assertThat(updatedAttempt.getName()).isEqualTo(solutionAttemptToUpdate.getName());
		assertThat(updatedAttempt.getDescription()).isEqualTo(solutionAttemptToUpdate.getDescription());
	}
	
	@Test
	public void deleteProblemSolutionAttemptByAttemptId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		
		this.problemService.deleteSolutionAttempt(
				createdProblem.getId(), createdSolutionAttempt.getId());
		Optional<SolutionAttempt> fetchedAttempt = 
				this.solutionAttemptRepository.findById(createdSolutionAttempt.getId());
		
		
		assertThat(fetchedAttempt.isPresent()).isFalse();	
	}
	
	@Test 
	public void updateProblemSolutionAttemptTechniques_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		Technique createdTechnique = this.buildAndSaveASipleTechcnique();
		Set<Technique> techniques = new HashSet<Technique>();
		techniques.add(createdTechnique);
		
		this.problemService.updateSolutionAttemptTechniques(
				createdProblem.getId(), createdSolutionAttempt.getId(), techniques);
		
		SolutionAttempt updatedAttempt = 
				this.solutionAttemptRepository.findById(createdSolutionAttempt.getId()).get();
		
		
		assertThat(updatedAttempt).isNotNull();
		assertThat(updatedAttempt.getTechniques()).contains(createdTechnique);
	}
	
	@Test 
	public void findAllProblemSolutionAttemptTechniques_withPageable_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		
		Technique createdTechnique = this.buildAndSaveASipleTechcnique();
		Set<Technique> techniques = new HashSet<Technique>();
		techniques.add(createdTechnique);
		
		createdSolutionAttempt.setTechniques(techniques);
		this.solutionAttemptRepository.save(createdSolutionAttempt);
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<Technique> fetchedAttempt = 
				this.problemService.findAllSolutionAttemptTechniques(
						createdProblem.getId(), createdSolutionAttempt.getId(), pageable);
		
		assertThat(fetchedAttempt).isNotNull();
		assertThat(fetchedAttempt.getContent()).contains(createdTechnique);
	}
	
	@Test 
	public void createProblemSolutionAttemptComment_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		SolutionAttemptComment attemptComment = this.buildASimpleSolutionAttemptComment();
		
		SolutionAttemptComment createdComment = this.problemService.createSolutionAttemptComment(
				createdProblem.getId(), createdSolutionAttempt.getId(), attemptComment);
		SolutionAttempt updatedAttempt = 
				this.solutionAttemptRepository.findById(createdSolutionAttempt.getId()).get();
		
		assertThat(createdComment).isNotNull();
		assertThat(createdComment.getId()).isNotNull();
		assertThat(createdComment.getContent()).isEqualTo(attemptComment.getContent());
		assertThat(updatedAttempt.getComments()).contains(createdComment);
	}
	
	@Test 
	public void findAllProblemSolutionAttemptComments_withPageable_shouldWork() {		
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		SolutionAttemptComment createdComment = 
				this.buildAndSaveASimpleSolutionAttemptCommentWithAAttempt(createdSolutionAttempt);
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<SolutionAttemptComment> solutionAttemptCommentsPage = 
				this.problemService.findAllSolutionAttemptComments(
						createdProblem.getId(), createdSolutionAttempt.getId(), pageable);
		
		assertThat(solutionAttemptCommentsPage).isNotNull();
		assertThat(solutionAttemptCommentsPage.getContent()).isNotNull();
		assertThat(solutionAttemptCommentsPage.getContent()).contains(createdComment);
	}
	
	@Test 
	public void deleteProblemSolutionAttemptCommentByCommentId_shouldWork() {	
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdSolutionAttempt = 
				this.buildAndSaveASimpleSolutionAttemptWithAProblem(createdProblem);
		SolutionAttemptComment createdComment = 
				this.buildAndSaveASimpleSolutionAttemptCommentWithAAttempt(createdSolutionAttempt);
		
		this.problemService.deleteSolutionAttemptComment(
				createdProblem.getId(), createdSolutionAttempt.getId(), createdComment.getId());
		Optional<SolutionAttemptComment> fetchedComment = 
				this.solutionAttemptCommentRepository.findById(createdSolutionAttempt.getId());
		
		
		assertThat(fetchedComment.isPresent()).isFalse();	
	}
	
	private Problem buildASimpleProblem() {
		return new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();
	}
	
	private Problem buildAndSaveASimpleProblem() {		
		return  this.problemRepository.save(this.buildASimpleProblem());
	}
	
	private SolutionAttempt buildASimpleSulutionAttempt() {
		return new SolutionAttemptBuilder()
				.withName("Utilizando a técnica Z")
				.withDescription("A tentativa do solução com a técnica...")
				.build();
	}
	
	private SolutionAttempt buildASimpleSulutionAttemptWithAProblem(Problem problem) {
		SolutionAttempt attempt = this.buildASimpleSulutionAttempt();
		attempt.setProblem(problem);
		return attempt;
	}

	
	private SolutionAttempt buildAndSaveASimpleSolutionAttemptWithAProblem(Problem problem) {
		SolutionAttempt soluttionAttempt = this.buildASimpleSulutionAttempt();	
		soluttionAttempt.setProblem(problem);	
		return this.solutionAttemptRepository.save(soluttionAttempt);
	}
	
	private Technique buildAndSaveASipleTechcnique() {
		Technique technique = new TechniqueBuilder()
				.withName("Técnica Z")
				.withDescription("A tecnica Z consiste em...")
				.build();
		return this.techniqueRepository.save(technique);
	}
	
	private SolutionAttemptComment buildASimpleSolutionAttemptComment() {
		return new SolutionAttemptComment("Comantário para a tentiva de solução");
	}
	
	private SolutionAttemptComment buildAndSaveASimpleSolutionAttemptCommentWithAAttempt(
			SolutionAttempt solutionAttempt) {
		SolutionAttemptComment comment = this.buildASimpleSolutionAttemptComment();
		comment.setSolutionAttempt(solutionAttempt);
		return this.solutionAttemptCommentRepository.save(comment);
	}
	
	private Concept buildAndSaveASimpleConcept() {
		Concept concept = new ConceptBuilder()
				.withName("Nome do conceito")
				.withDescription("Descrição do conceito")
				.build();
		return this.conceptService.create(concept);
	}
	
	private ProblemComment buildASimpleProblemCommentWithAProblem(Problem problem) {
		ProblemComment comment = new ProblemComment("Comentário para o problema");
		comment.setProblem(problem);
		return comment;
	}
	
	private Goal buildASimpleGoalWithAProblem(Problem problem) {
		Goal goal = new Goal("Objetivo M");
		goal.setProblem(problem);
		return goal;
	}
	
	private Goal buildAndSaveASimpleGoalWithAProblem(Problem problem) {
		return this.goalRepository.save(
				this.buildASimpleGoalWithAProblem(problem));
	}
}

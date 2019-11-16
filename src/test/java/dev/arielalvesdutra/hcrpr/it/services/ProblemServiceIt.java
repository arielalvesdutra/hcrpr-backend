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
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.builders.ConceptBuilder;
import dev.arielalvesdutra.hcrpr.builders.ProblemBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.repositories.GoalRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;
import dev.arielalvesdutra.hcrpr.services.ConceptService;
import dev.arielalvesdutra.hcrpr.services.ProblemService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
public class ProblemServiceIt {
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private GoalRepository goalRepository;
	
	@Autowired
	private ProblemCommentRepository problemCommentRepository;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private ConceptRepository conceptRepository;
	
	@After
	public void tearDown() {
		this.problemRepository.deleteAll();
		this.conceptRepository.deleteAll();
		this.problemCommentRepository.deleteAll();
		this.goalRepository.deleteAll();
	}
	
	@Test
	public void createProblem_withAllRelations_shouldWork() {
		throw new RuntimeException("Método não implementado.");
	}
	
	@Test
	public void createProblem_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();
		
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
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		this.problemRepository.save(problem);	
		
		List<Problem> fetchedProblems = this.problemService.findAll();
		
		assertThat(fetchedProblems).isNotNull();
		assertThat(fetchedProblems.contains(problem)).isTrue();
	}
	
	@Test
	public void findAll_withPageable_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<Problem> fetchedProblemPage = this.problemService.findAll(pageable);
		
		assertThat(fetchedProblemPage).isNotNull();
		assertThat(fetchedProblemPage.getContent().contains(createdProblem)).isTrue();
	}
	
	@Test
	public void findById_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
		
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
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
		
		this.problemService.deleteById(createdProblem.getId());
		Optional<Problem> fetchedConcept = 
					this.problemRepository.findById(createdProblem.getId());
		
		assertThat(fetchedConcept.isPresent()).isFalse();
	}
	
	
	@Test
	public void updateProblem_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
		
		
		createdProblem.setName("Problema modificado");
		createdProblem.setDescription("Descrição modificada");
		Problem updatedConcept =
				this.problemService.update(createdProblem.getId(), createdProblem);
		
		
		assertThat(updatedConcept).isNotNull();
		assertThat(updatedConcept.getId()).isEqualTo(createdProblem.getId());
		assertThat(updatedConcept.getName()).isEqualTo(createdProblem.getName());
		assertThat(updatedConcept.getDescription()).isEqualTo(createdProblem.getDescription());
		assertThat(updatedConcept.getCreatedAt()).isEqualTo(createdProblem.getCreatedAt());
	}
	
	@Test
	public void updateProblemRelatedConcepts_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
		Concept concept = new ConceptBuilder()
				.withName("Nome do conceito")
				.withDescription("Descrição do conceito")
				.build();
		Concept createdConcept = this.conceptService.create(concept);
		Set<Concept> concepts = new HashSet<Concept>(); 
		concepts.add(createdConcept);
		
		
		this.problemService.updateProblemRelatedConcepts(createdProblem.getId(), concepts);
		Problem updatedProblem = this.problemService.findById(createdProblem.getId());
		
		
		assertThat(updatedProblem.getRelatedConcepts()).isNotNull();
		assertThat(updatedProblem.getRelatedConcepts()).contains(createdConcept);
	}
	
	@Test
	public void findAllProblemRelatedConcepts_withPageable_shouldWork() {
		Concept concept = new ConceptBuilder()
				.withName("Nome do conceito")
				.withDescription("Descrição do conceito")
				.build();
		Concept createdConcept = this.conceptService.create(concept);
		Set<Concept> concepts = new HashSet<Concept>(); 
		concepts.add(createdConcept);
		
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.withRelatedConcepts(concepts)
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<Concept> problemRelatedConceptsPage = 
					this.problemService.findAllProblemRelatedConcepts(createdProblem.getId(), pageable);
		
		
		assertThat(problemRelatedConceptsPage).isNotNull();
		assertThat(problemRelatedConceptsPage.getContent()).contains(createdConcept);
	}
	
	@Test
	public void createProblemComment_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);	
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
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);
		
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
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);
		
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
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();
		Problem createdProblem = this.problemRepository.save(problem);	
		Goal goal = new Goal("Resolver questão X");
		
		Goal createdGoal = this.problemService.createProblemGoal(createdProblem.getId(), goal);
		Problem updatedProblem = this.problemRepository.findById(createdProblem.getId()).get();
		
		assertThat(createdGoal).isNotNull();
		assertThat(createdGoal.getId()).isNotNull();
		assertThat(createdGoal.getDescription()).isEqualTo(goal.getDescription());		
		assertThat(updatedProblem.getGoals()).contains(createdGoal);
	}
	
	@Test
	public void findAllProblemGoals_withPageable_shouldWork() {
		Problem problem = new ProblemBuilder()
				.withName("Distração nos estudos")
				.withDescription("A distração nos estudos prejudica o desempenho...")
				.build();		
		Problem createdProblem = this.problemRepository.save(problem);
		
		Goal goal = new Goal("Resolver questão X");
		goal.setProblem(createdProblem);
		Goal createdGoal = this.goalRepository.save(goal);

		Pageable pageable = PageRequest.of(0, 10, Sort.by("content"));
		
		Page<Goal> goalsPage = 
				this.problemService.findAllProblemGoals(createdProblem.getId(), pageable);
		
		
		assertThat(goalsPage).isNotNull();
		assertThat(goalsPage.getContent()).isNotNull();
		assertThat(goalsPage).contains(createdGoal);
	}
}

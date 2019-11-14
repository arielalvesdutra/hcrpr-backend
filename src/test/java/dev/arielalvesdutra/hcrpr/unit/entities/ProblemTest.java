package dev.arielalvesdutra.hcrpr.unit.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;

@RunWith(SpringRunner.class)
public class ProblemTest {
	
	@Test
	public void setAndGetId_shouldWork() {
		Long id = 1L;
		Problem problem = new Problem();
		problem.setId(id);
		
		assertThat(problem.getId()).isEqualTo(id);
	}
	
	@Test
	public void setAndGetName_shouldWork() {
		String name = "Distração nos estudos";
		Problem problem = new Problem();
		problem.setName(name);
		
		assertThat(problem.getName()).isEqualTo(name);
	}
	
	@Test
	public void setAndGetDescription_shouldWork() {
		String description = "Distração nos estudos ocorre quando...";
		Problem problem = new Problem();
		problem.setDescription(description);
		
		assertThat(problem.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		OffsetDateTime createdAt = OffsetDateTime.now();
		Problem problem = new Problem();
		problem.setCreatedAt(createdAt);
		
		assertThat(problem.getCreatedAt()).isEqualTo(createdAt);
	}
	
	@Test
	public void setAndGetComments_shouldWork() {
		Problem problem = new Problem();
		ProblemComment comment1 = new ProblemComment("Comentário1 do problema");
		
		Set<ProblemComment> comments = new HashSet<ProblemComment>();
		comments.add(comment1);
		
		
		problem.setComments(comments);
				
		assertThat(problem.getComments().contains(comment1)).isTrue();		
	}
	
	@Test
	public void setAndGetRelatedConcepts_shouldWork() {
		Problem problem = new Problem();
		Concept concept1 = new Concept("Grandes números e Estatística");
		Set<Concept> concepts = new HashSet<Concept>();
		concepts.add(concept1);
		
		problem.setRelatedConcepts(concepts);
		
		
		assertThat(problem.getRelatedConcepts().contains(concept1)).isTrue();
	}
	
	@Test
	public void setAndGetGoals_shouldWork() {
		Problem problem = new Problem();
		Goal goal1 = new Goal("Melhor desempenho dos estudos nos sábados");
		Set<Goal> goals = new HashSet<Goal>();
		goals.add(goal1);
		
		problem.setGoals(goals);
		
		assertThat(problem.getGoals().contains(goal1)).isTrue();
	}
	
	@Test
	public void setAndGetSolutionAttempts_shouldWork() {
		Problem problem = new Problem("Distração no Estudo");
		SolutionAttempt solutionAttempt = 
					new SolutionAttempt("Uso da Técnica da Pomodoro");
		Set<SolutionAttempt> solutionAttempts = new HashSet<SolutionAttempt>();
		solutionAttempts.add(solutionAttempt);
		
		problem.setSolutionAttempts(solutionAttempts);
		
		assertThat(problem.getSolutionAttempts().contains(solutionAttempt))
			.isTrue();
	}
	
	@Test
	public void setAndGetRelatedProblems_shouldWork() {
		Problem problem = new Problem("Distração nos estudos");
		Problem relatedProblem = new Problem("Distração no trabalho");
		Set<Problem> relatedProblems = new HashSet<Problem>();
		relatedProblems.add(relatedProblem);
		
		problem.setRelatedProblems(relatedProblems);
		
		assertThat(problem.getRelatedProblems().contains(relatedProblem)).isTrue();
	}
	
	@Test
	public void equals_shouldBeById() {
		Long id = 1L;
		Problem problem1 = new Problem();
		Problem problem2 = new Problem();
		
		
		problem1.setId(id);
		problem2.setId(id);
		
		
		assertThat(problem1).isEqualTo(problem2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new Problem();
	}
	
	@Test
	public void mustHaveEntityAnnotation() {
		assertThat(Problem.class.isAnnotationPresent(Entity.class)).isTrue();
	}
	
	@Test
	public void mustImplementSerializable() {
		assertThat(new Problem() instanceof Serializable).isTrue();
	}
	
	@Test
	public void id_mustHaveIdAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isIdAnnotationPresent = 
				Problem.class.getDeclaredField("id").isAnnotationPresent(Id.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
	
	@Test
	public void relatedConcepts_mustHaveManyToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToManyAnnotationPresent = Problem.class
				.getDeclaredField("relatedConcepts")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isManyToManyAnnotationPresent).isTrue();
	}
	
	@Test
	public void goals_mustHaveOneToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isOneToManyAnnotationPresent = Problem.class
				.getDeclaredField("goals")
				.isAnnotationPresent(OneToMany.class);
		
		
		assertThat(isOneToManyAnnotationPresent).isTrue();
	}
	
	@Test
	public void solutionAttempts_mustHaveOneToManyAnnotation_withCurrentConfiguration() 
			throws NoSuchFieldException, SecurityException {

		OneToMany oneToMany = Problem.class
				.getDeclaredField("solutionAttempts")
				.getAnnotation(OneToMany.class);
		
		
		assertThat(oneToMany).isNotNull();
		assertThat(oneToMany.mappedBy()).isEqualTo("problem");
	}
	
	@Test
	public void relatedProblems_mustHaveManyToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToManyAnnotationPresent = Problem.class
				.getDeclaredField("relatedProblems")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isManyToManyAnnotationPresent).isTrue();
	}
	
	@Test
	public void relatedConcepts_mustHaveJoinTableAnnotation_withCurrentConfiguration()
			throws NoSuchFieldException, SecurityException {
		
		JoinTable joinTable = Problem.class
				.getDeclaredField("relatedConcepts")
				.getAnnotation(JoinTable.class);

		JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
		JoinColumn joinColumn = joinTable.joinColumns()[0];		
		
		assertThat(joinTable.name()).isEqualTo("problem_concept");
		assertThat(inverseJoinColumn.name()).isEqualTo("problem_id");
		assertThat(inverseJoinColumn.referencedColumnName()).isEqualTo("id");
		assertThat(joinColumn.name()).isEqualTo("concept_id");
		assertThat(joinColumn.referencedColumnName()).isEqualTo("id");
	}
	
	@Test
	public void relatedProblems_mustHaveJoinTableAnnotation_withCurrentConfiguration() 
			throws NoSuchFieldException, SecurityException {
		
		JoinTable joinTable = Problem.class
				.getDeclaredField("relatedProblems")
				.getAnnotation(JoinTable.class);

		JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
		JoinColumn joinColumn = joinTable.joinColumns()[0];		
		
		assertThat(joinTable.name()).isEqualTo("problem_problem");
		assertThat(inverseJoinColumn.name()).isEqualTo("problem_id");
		assertThat(inverseJoinColumn.referencedColumnName()).isEqualTo("id");
		assertThat(joinColumn.name()).isEqualTo("related_problem_id");
		assertThat(joinColumn.referencedColumnName()).isEqualTo("id");
	}
	
	@Test
	public void goals_mustHaveOneToManyAnnotation_withCurrentConfiguration() 
			throws NoSuchFieldException, SecurityException {
		
		OneToMany oneToMany = Problem.class
				.getDeclaredField("goals")
				.getAnnotation(OneToMany.class);
		
		
		assertThat(oneToMany).isNotNull();
		assertThat(oneToMany.mappedBy()).isEqualTo("problem");
	
	}
}

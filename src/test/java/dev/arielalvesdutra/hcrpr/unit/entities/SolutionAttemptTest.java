package dev.arielalvesdutra.hcrpr.unit.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;

@RunWith(SpringRunner.class)
public class SolutionAttemptTest {

	@Test
	public void getAndSetId_shouldWork() {
		Long id = 1L;
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		solutionAttempt.setId(id);
		
		assertThat(solutionAttempt.getId()).isEqualTo(id);
	}
	
	@Test
	public void getAndSetName_shouldWork() {
		String name = "Tentativa utilizando técnica X";
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		solutionAttempt.setName(name);
		
		assertThat(solutionAttempt.getName()).isEqualTo(name);
	}
	
	@Test
	public void setAndGetDescription_shouldWork() {
		String description = "Distração nos estudos ocorre quando...";
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		solutionAttempt.setDescription(description);
		
		assertThat(solutionAttempt.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		OffsetDateTime createdAt = OffsetDateTime.now();
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		solutionAttempt.setCreatedAt(createdAt);
		
		assertThat(solutionAttempt.getCreatedAt()).isEqualTo(createdAt);
	}
	
	@Test
	public void setAndGetTendency_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		String tendency = "1) Olhar o site.com as 9:00. 2) Usar o celular...";
		solutionAttempt.setTendency(tendency);
		
		assertThat(solutionAttempt.getTendency()).isEqualTo(tendency);
	}
	
	@Test
	public void setAndGetLearned_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		String learned = "O aprendizado dessa tentativa é...";
		solutionAttempt.setLearned(learned);
		
		assertThat(solutionAttempt.getLearned()).isEqualTo(learned);
	}
	
	@Test
	public void setAndGetComments_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		SolutionAttemptComment comment1 = new SolutionAttemptComment("Comentário1 da tentativa de solução");
		
		Set<SolutionAttemptComment> comments = new HashSet<SolutionAttemptComment>();
		comments.add(comment1);
		
		
		solutionAttempt.setComments(comments);
				
		assertThat(solutionAttempt.getComments().contains(comment1)).isTrue();		
	}
	
	@Test
	public void setAndGetProblem_shouldWork() {
		Problem problem = new Problem("Distração nos estudos");
		SolutionAttempt solutionAttempt = new SolutionAttempt("Utilizar a Técnica de Pomodoro");
		
		solutionAttempt.setProblem(problem);
		
		assertThat(solutionAttempt.getProblem()).isEqualTo(problem);		
	}
	
	@Test
	public void setAndGetTechniques_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt("Utilizar a Técnica de Pomodoro");
		Technique technique = new Technique("Pomodoro");
		Set<Technique> techniques = new HashSet<Technique>();
		techniques.add(technique);
		
		solutionAttempt.setTechniques(techniques);
		
		
		assertThat(solutionAttempt.getTechniques().contains(technique)).isTrue();
	}
	
	@Test
	public void equals_shouldBeById() {
		SolutionAttempt solutionAttempt1 = new SolutionAttempt();
		SolutionAttempt solutionAttempt2 = new SolutionAttempt();
		Long id = 1L;		
		
		
		solutionAttempt1.setId(id);
		solutionAttempt2.setId(id);
		
		
		assertThat(solutionAttempt1).isEqualTo(solutionAttempt2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new SolutionAttempt();
	}
	
	@Test
	public void mustHaveEntityAnnotation() {
		assertThat(SolutionAttempt.class.isAnnotationPresent(Entity.class)).isTrue();
	}
	
	@Test
	public void id_mustHaveIdAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isIdAnnotationPresent = 
				SolutionAttempt.class.getDeclaredField("id").isAnnotationPresent(Id.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
	
	@Test
	public void mustImplementSerializable() {
		assertThat(new SolutionAttempt() instanceof Serializable).isTrue();
	}
	
	@Test
	public void problem_mustHaveManyToOneAnnotation() 
			throws NoSuchFieldException, SecurityException {
		
		boolean isManyToOneAnnotationPresent = SolutionAttempt.class
				.getDeclaredField("problem")
				.isAnnotationPresent(ManyToOne.class);
		
		
		assertThat(isManyToOneAnnotationPresent).isTrue();
	}
	
	@Test
	public void techniques_mustHaveManyToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToManyAnnotationPresent = SolutionAttempt.class
				.getDeclaredField("techniques")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isManyToManyAnnotationPresent).isTrue();
	}
	
	@Test
	public void techniques_mustHaveJoinTableAnnotation_withCurrentConfiguration()
			throws NoSuchFieldException, SecurityException {
		
		JoinTable joinTable = SolutionAttempt.class
				.getDeclaredField("techniques")
				.getAnnotation(JoinTable.class);

		JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
		JoinColumn joinColumn = joinTable.joinColumns()[0];		
		
		assertThat(joinTable.name()).isEqualTo("solution_attempt_technique");
		assertThat(inverseJoinColumn.name()).isEqualTo("technique_id");
		assertThat(inverseJoinColumn.referencedColumnName()).isEqualTo("id");
		assertThat(joinColumn.name()).isEqualTo("solution_attempt_id");
		assertThat(joinColumn.referencedColumnName()).isEqualTo("id");
	}
	
	@Test
	public void techniques_mustHaveJsonIgnoreAnnotation() 
			throws NoSuchFieldException, SecurityException {
		boolean isJsonIgnoreAnnotationPresent = SolutionAttempt.class
				.getDeclaredField("techniques")
				.isAnnotationPresent(JsonIgnore.class);
		
		
		assertThat(isJsonIgnoreAnnotationPresent).isTrue();
	}
	
	@Test
	public void comments_mustHaveJsonIgnoreAnnotation() 
			throws NoSuchFieldException, SecurityException {
		boolean isJsonIgnoreAnnotationPresent = SolutionAttempt.class
				.getDeclaredField("comments")
				.isAnnotationPresent(JsonIgnore.class);
		
		
		assertThat(isJsonIgnoreAnnotationPresent).isTrue();
	}
	
	@Test
	public void problem_mustHaveJsonIgnoreAnnotation() 
			throws NoSuchFieldException, SecurityException {
		boolean isJsonIgnoreAnnotationPresent = SolutionAttempt.class
				.getDeclaredField("problem")
				.isAnnotationPresent(JsonIgnore.class);
		
		
		assertThat(isJsonIgnoreAnnotationPresent).isTrue();
	}
	
	@Test
	public void description_mustHaveColumnAnnotationWithTextDefinition() 
			throws NoSuchFieldException, SecurityException {
		Column column = SolutionAttempt.class
				.getDeclaredField("description")
				.getAnnotation(Column.class);
		
		
		assertThat(column).isNotNull();
		assertThat(column.columnDefinition()).isEqualTo("TEXT");
	}
}

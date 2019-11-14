package dev.arielalvesdutra.hcrpr.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
		boolean isIdAnnotationPresent = Problem.class
				.getDeclaredField("relatedConcepts")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
}

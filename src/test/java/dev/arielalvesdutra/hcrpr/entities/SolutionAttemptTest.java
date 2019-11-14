package dev.arielalvesdutra.hcrpr.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
	public void setAndGetComments_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		SolutionAttemptComment comment1 = new SolutionAttemptComment("Comentário1 da tentativa de solução");
		
		Set<SolutionAttemptComment> comments = new HashSet<SolutionAttemptComment>();
		comments.add(comment1);
		
		
		solutionAttempt.setComments(comments);
				
		assertThat(solutionAttempt.getComments().contains(comment1)).isTrue();		
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
}

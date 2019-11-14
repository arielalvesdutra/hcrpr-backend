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
public class TechniqueTest {
	
	@Test
	public void setAndGetId_shouldWork() {
		Long id = 1L;
		Technique technique = new Technique();
		technique.setId(id);
		
		assertThat(technique.getId()).isEqualTo(id);
	}
	
	@Test
	public void setAndGetName_shouldWork() {
		String name = "Técnica de Pomodoro";
		Technique technique = new Technique();
		technique.setName(name);
		
		assertThat(technique.getName()).isEqualTo(name);
	}
	
	@Test
	public void setAndGetDescription_shouldWork() {
		String description = "A Técnica Pomodoro é um método de gerenciamento de ...";
		Technique technique = new Technique();
		technique.setDescription(description);
		
		assertThat(technique.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		Technique technique = new Technique();
		OffsetDateTime createdAt = OffsetDateTime.now();
		
		technique.setCreatedAt(createdAt);
		
		assertThat(technique.getCreatedAt()).isEqualTo(createdAt);
	}
	
	@Test
	public void setAndGetSolutionAttempts_shouldWork() {
		Technique technique = new Technique("Pomodoro");
		SolutionAttempt solutionAttempt = new SolutionAttempt("Utilizar a Técnica de Pomodoro");
		
		Set<SolutionAttempt> solutionAttempts = new HashSet<SolutionAttempt>();
		solutionAttempts.add(solutionAttempt);
		
		technique.setSolutionAttempts(solutionAttempts);
		
		
		assertThat(technique.getSolutionAttempts().contains(solutionAttempt)).isTrue();
	}
	
	@Test
	public void equals_shouldBeById() {
		Technique technique1 = new Technique();
		Technique technique2 = new Technique();
		Long id = 1L;		
		
		
		technique1.setId(id);
		technique2.setId(id);
		
		
		assertThat(technique1).isEqualTo(technique2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new Technique();
	}
	
	@Test
	public void mustHaveEntityAnnotation() {
		assertThat(Technique.class.isAnnotationPresent(Entity.class)).isTrue();
	}
	
	@Test
	public void mustImplementSerializable() {
		assertThat(new Technique() instanceof Serializable).isTrue();
	}
	
	@Test
	public void id_mustHaveIdAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isIdAnnotationPresent = 
				Technique.class.getDeclaredField("id").isAnnotationPresent(Id.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
	
	@Test
	public void solutionAttempts_mustHaveManyToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToManyAnnotationPresent = Technique.class
				.getDeclaredField("solutionAttempts")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isManyToManyAnnotationPresent).isTrue();
	}
}

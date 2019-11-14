package dev.arielalvesdutra.hcrpr.unit.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;

@RunWith(SpringRunner.class)
public class GoalTest {
	
	@Test
	public void setAndGetId_shouldWork() {
		Long id = 1L;
		Goal goal = new Goal();
		goal.setId(id);
		
		assertThat(goal.getId()).isEqualTo(id);
	}
	
	@Test
	public void setAndGetDescription_shouldWork() {
		String description = "Resolver o problema X";
		Goal goal = new Goal();
		goal.setDescription(description);
		
		assertThat(goal.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void setAndGetAchieved_shouldWork() {
		Boolean isAchieved =  true;
		Goal goal = new Goal();
		
		goal.setAchieved(isAchieved);
		
		assertThat(goal.getAchieved()).isTrue();
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		Goal goal = new Goal();
		OffsetDateTime createdAt = OffsetDateTime.now();
		
		goal.setCreatedAt(createdAt);
		
		assertThat(goal.getCreatedAt()).isEqualTo(createdAt);
	}
	
	@Test
	public void setAndGetProblem_shouldWork() {
		Problem problem = new Problem("Distração no estudo");
		Goal goal = new Goal("Até 20 minutos para ver conteúdos não relacionados por dia");
		
		goal.setProblem(problem);
		
		assertThat(goal.getProblem()).isEqualTo(problem);
	}
	
	@Test
	public void equals_shouldBeById() {
		Long id = 1L;
		Goal goal1 = new Goal();
		Goal goal2 = new Goal();
		
		goal1.setId(id);
		goal2.setId(id);
		
		
		assertThat(goal1).isEqualTo(goal2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new Goal();
	}
	
	
	@Test
	public void mustHaveEntityAnnotation() {
		assertThat(Goal.class.isAnnotationPresent(Entity.class)).isTrue();
	}
	
	@Test
	public void mustImplementSerializable() {
		assertThat(new Goal() instanceof Serializable).isTrue();
	}
	
	@Test
	public void id_mustHaveIdAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isIdAnnotationPresent = Goal.class
				.getDeclaredField("id")
				.isAnnotationPresent(Id.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
	
	@Test
	public void problem_mustHaveManyToOneAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToOneAnnotationPresent = Goal.class
				.getDeclaredField("problem")
				.isAnnotationPresent(ManyToOne.class);
		
		
		assertThat(isManyToOneAnnotationPresent).isTrue();
	}
}

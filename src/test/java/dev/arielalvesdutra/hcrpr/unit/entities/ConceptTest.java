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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Problem;

@RunWith(SpringRunner.class)
public class ConceptTest {

	@Test
	public void setAndGetId_shouldWork() {
		Long id = 1L;
		Concept concept = new Concept();
		concept.setId(id);
		
		assertThat(concept.getId()).isEqualTo(id);
	}
	
	@Test
	public void setAndGetName_shouldWork() {
		String name = "Viés de Confirmação";
		Concept concept = new Concept();
		concept.setName(name);
		
		assertThat(concept.getName()).isEqualTo(name);
	}
	
	@Test
	public void setAndGetDescription_shouldWork() {
		String description = "Viés de confirmação, também chamado de...";
		Concept concept = new Concept();
		concept.setDescription(description);
		
		assertThat(concept.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		Concept concept = new Concept();
		OffsetDateTime createdAt = OffsetDateTime.now();
		
		concept.setCreatedAt(createdAt);
		
		assertThat(concept.getCreatedAt()).isEqualTo(createdAt);
	}
	
	@Test
	public void setAndGetProblems_shouldWork() {
		Concept concept = new Concept("História e Memória");
		Problem problem = new Problem("Dormir muito tarde");
		Set<Problem> problems = new HashSet<Problem>();
		problems.add(problem);
		
		
		concept.setProblems(problems);
		
		
		assertThat(concept.getProblems().contains(problem)).isTrue();
	}
	
	@Test
	public void equals_shouldBeById() {
		Long id = 1L;
		Concept concept1 = new Concept();
		Concept concept2 = new Concept();
		
		concept1.setId(id);
		concept2.setId(id);
		
		assertThat(concept1).isEqualTo(concept2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new Concept();
	}
	
	@Test
	public void mustHaveEntityAnnotation() {
		assertThat(Concept.class.isAnnotationPresent(Entity.class)).isTrue();
	}
	
	@Test
	public void mustImplementSerializable() {
		assertThat(new Concept() instanceof Serializable).isTrue();
	}
	
	@Test
	public void id_mustHaveIdAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isIdAnnotationPresent = 
				Concept.class.getDeclaredField("id").isAnnotationPresent(Id.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
	
	@Test
	public void problems_mustHaveManyToManyAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isManyToManyAnnotationPresent = Concept.class
				.getDeclaredField("problems")
				.isAnnotationPresent(ManyToMany.class);
		
		
		assertThat(isManyToManyAnnotationPresent).isTrue();
	}
	
	@Test
	public void problems_mustHaveJsonIgnoreAnnotation() throws NoSuchFieldException, SecurityException {
		boolean isJsonIgnoreAnnotationPresent = Concept.class
				.getDeclaredField("problems")
				.isAnnotationPresent(JsonIgnore.class);
		
		
		assertThat(isJsonIgnoreAnnotationPresent).isTrue();
	}
	
	@Test
	public void problems_mustHaveJoinTableAnnotation_withCurrentConfiguration()
			throws NoSuchFieldException, SecurityException {
		
		JoinTable joinTable = Concept.class
				.getDeclaredField("problems")
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
	public void description_mustHaveColumnAnnotationWithTextDefinition() 
			throws NoSuchFieldException, SecurityException {
		Column column = Concept.class
				.getDeclaredField("description")
				.getAnnotation(Column.class);
		
		
		assertThat(column).isNotNull();
		assertThat(column.columnDefinition()).isEqualTo("TEXT");
	}
}

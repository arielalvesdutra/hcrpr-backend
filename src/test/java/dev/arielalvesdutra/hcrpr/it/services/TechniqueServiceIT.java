package dev.arielalvesdutra.hcrpr.it.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

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

import dev.arielalvesdutra.hcrpr.builders.TechniqueBuilder;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.TechniqueRepository;
import dev.arielalvesdutra.hcrpr.services.TechniqueService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
public class TechniqueServiceIT {

	@Autowired
	private TechniqueService techniqueService;
	
	@Autowired
	private TechniqueRepository techniqueRepository;
	
	@After
	public void tearDown() {
		this.techniqueRepository.deleteAll();
	}
	
	@Test
	public void createTechnique_shouldWork() {
		Technique technique = this.buildASimpleTechnique();
		
		Technique createdTechnique = this.techniqueService.create(technique);
		
		
		assertThat(createdTechnique).isNotNull();
		assertThat(createdTechnique.getName()).isEqualTo(technique.getName());
		assertThat(createdTechnique.getDescription()).isEqualTo(technique.getDescription());
		assertThat(createdTechnique.getId()).isNotNull();
		assertThat(createdTechnique.getCreatedAt())
				.describedAs("CreatedAt não pode ser nulo").isNotNull();
	}
	
	@Test
	public void findAll_shouldWork() {
		this.buildAndSaveASimpleTechnique();
				
		List<Technique> fetchedTechniques = this.techniqueService.findAll();
				
		assertThat(fetchedTechniques).isNotNull();
	}
	
	@Test
	public void findAll_withPageable_shouldWork() {
		Technique technique = new TechniqueBuilder()
				.withName("Matriz de Eisenhower")
				.withDescription("A matriz de Eisenhower categoriza afazeres em...")
				.build();
		this.techniqueRepository.save(technique);
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		
		Page<Technique> fetchedTechniques = this.techniqueService.findAll(pageable);
		
		
		assertThat(fetchedTechniques).isNotNull();
	}
	
	@Test
	public void findById_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		
		Technique fetchedTechnique = 
					this.techniqueService.findById(createdTechnique.getId());
		
		assertThat(fetchedTechnique).isNotNull();
		assertThat(fetchedTechnique.getId()).isEqualTo(createdTechnique.getId());
		assertThat(fetchedTechnique.getName()).isEqualTo(createdTechnique.getName());
		assertThat(fetchedTechnique.getDescription()).isEqualTo(createdTechnique.getDescription());
		assertThat(fetchedTechnique.getCreatedAt()).isEqualTo(createdTechnique.getCreatedAt());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void findById_withoutTechniques_shouldThrownAnEntityNotFoundException() {
		Long id = 1L;
		this.techniqueService.findById(id);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		
 
		this.techniqueService.deleteById(createdTechnique.getId());
		Optional<Technique> fetchedTechnique = 
					this.techniqueRepository.findById(createdTechnique.getId());
		
		assertThat(fetchedTechnique.isPresent()).isFalse();
	}
	
	@Test
	public void updateTechnique_shouldWork() {
		Technique createdTechnique = this.buildAndSaveASimpleTechnique();
		
		
		createdTechnique.setName("Matriz modificada");
		createdTechnique.setDescription("Descrição modificada");
		Technique updatedTechnique =
				this.techniqueService.update(createdTechnique.getId(), createdTechnique);
		
		
		assertThat(updatedTechnique).isNotNull();
		assertThat(updatedTechnique.getId()).isEqualTo(createdTechnique.getId());
		assertThat(updatedTechnique.getName()).isEqualTo(createdTechnique.getName());
		assertThat(updatedTechnique.getDescription()).isEqualTo(createdTechnique.getDescription());
		assertThat(updatedTechnique.getCreatedAt()).isEqualTo(createdTechnique.getCreatedAt());
	}
	
	private Technique buildASimpleTechnique() {
		return new TechniqueBuilder()
				.withName("Matriz de Eisenhower")
				.withDescription("A matriz de Eisenhower categoriza afazeres em...")
				.build();
	}
	
	private Technique buildAndSaveASimpleTechnique() {
		return this.techniqueRepository.save(
				this.buildASimpleTechnique());
	}
}

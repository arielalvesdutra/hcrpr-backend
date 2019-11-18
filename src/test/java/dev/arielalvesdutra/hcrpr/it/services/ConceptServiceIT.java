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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.builders.ConceptBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.services.ConceptService;

@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ConceptServiceIT {
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private ConceptRepository conceptRepository;
	
	@After
	public void tearDown() {
		this.conceptRepository.deleteAll();
	}
	
	@Test
	public void createConcept_shouldWork() {
		Concept concept = this.buildASimpleConcept();
		
		Concept createdConcept = this.conceptService.create(concept);
		
		assertThat(createdConcept).isNotNull();
		assertThat(createdConcept.getName()).isEqualTo(concept.getName());
		assertThat(createdConcept.getDescription()).isEqualTo(concept.getDescription());
		assertThat(createdConcept.getId()).isNotNull();
		assertThat(createdConcept.getCreatedAt())
				.describedAs("CreatedAt não pode ser nulo").isNotNull();
	}
	
	@Test
	public void findAll_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		
		List<Concept> fetchedConcepts = this.conceptService.findAll();
		
		
		assertThat(fetchedConcepts).isNotNull();
		assertThat(fetchedConcepts.contains(createdConcept)).isTrue();
	}
	
	
	@Test
	public void findAll_withPageable_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		
		Page<Concept> fetchedConceptsPage = this.conceptService.findAll(pageable);
		
		
		assertThat(fetchedConceptsPage).isNotNull();
		assertThat(fetchedConceptsPage.getContent().contains(createdConcept)).isTrue();
	}
	
	@Test
	public void findById_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		
		Concept fetchedConcept = 
					this.conceptService.findById(createdConcept.getId());
		
		assertThat(fetchedConcept).isNotNull();
		assertThat(fetchedConcept.getId()).isEqualTo(createdConcept.getId());
		assertThat(fetchedConcept.getName()).isEqualTo(createdConcept.getName());
		assertThat(fetchedConcept.getDescription()).isEqualTo(createdConcept.getDescription());
		assertThat(fetchedConcept.getCreatedAt()).isEqualTo(createdConcept.getCreatedAt());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void findById_withoutConcepts_shouldThrownAnEntityNotFoundException() {
		Long id = 1L;
		this.conceptService.findById(id);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		
 
		this.conceptService.deleteById(createdConcept.getId());
		Optional<Concept> fetchedConcept = 
					this.conceptRepository.findById(createdConcept.getId());
		
		assertThat(fetchedConcept.isPresent()).isFalse();
	}
	
	@Test
	public void updateConcept_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		
		
		createdConcept.setName("Conceito modificado");
		createdConcept.setDescription("Descrição modificada");
		Concept updatedConcept =
				this.conceptService.update(createdConcept.getId(), createdConcept);
		
		
		assertThat(updatedConcept).isNotNull();
		assertThat(updatedConcept.getId()).isEqualTo(createdConcept.getId());
		assertThat(updatedConcept.getName()).isEqualTo(createdConcept.getName());
		assertThat(updatedConcept.getDescription()).isEqualTo(createdConcept.getDescription());
		assertThat(updatedConcept.getCreatedAt()).isEqualTo(createdConcept.getCreatedAt());
	}
	
	private Concept buildASimpleConcept() {
		return new ConceptBuilder()
				.withName("Regressão à média")
				.withDescription("Em estatística, a regressão à média é o fenómeno...")
				.build();	
	}
	
	private Concept buildAndSaveASimpleConcept() {
		return this.conceptRepository.save(this.buildASimpleConcept());
	}

}

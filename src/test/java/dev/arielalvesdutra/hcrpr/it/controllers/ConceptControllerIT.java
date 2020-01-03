package dev.arielalvesdutra.hcrpr.it.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import dev.arielalvesdutra.hcrpr.builders.ConceptBuilder;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateConceptDTO;
import dev.arielalvesdutra.hcrpr.builders.dto.builders.CreateConceptDTOBuilder;
import dev.arielalvesdutra.hcrpr.builders.dto.builders.UpdateConceptDTOBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.services.ConceptService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ConceptControllerIT {

	@Autowired
	private ConceptRepository conceptRepository;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@After
	public void tearDown() {
		conceptRepository.deleteAll();
	}
	
	@Test
	public void create_shouldWorkAndReturn201() {
		CreateConceptDTO createConceptDto = new CreateConceptDTOBuilder()
				.withName("Conceito X")
				.withDescription("Descrição do conceito X")
				.build();
		
		
		ResponseEntity<RetrieveConceptDTO> response = 
				restTemplate.postForEntity("/concepts", createConceptDto, RetrieveConceptDTO.class);
		RetrieveConceptDTO retrievedConceptDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(retrievedConceptDto.getName()).isEqualTo(createConceptDto.getName());
		assertThat(retrievedConceptDto.getDescription()).isEqualTo(createConceptDto.getDescription());
		assertThat(retrievedConceptDto.getCreatedAt()).isNotNull();
		assertThat(retrievedConceptDto.getId()).isNotNull();
	}
	
	@Test
	public void create_withoutName_shouldReturn400() {
		CreateConceptDTO createConceptDto = new CreateConceptDTOBuilder()
				.withDescription("Descrição do conceito X")
				.build();
		
		
		ResponseEntity<RetrieveConceptDTO> response = 
				restTemplate.postForEntity("/concepts", createConceptDto, RetrieveConceptDTO.class);
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	public void create_withoutDescription_shouldReturn400() {
		CreateConceptDTO createConceptDto = new CreateConceptDTOBuilder()
				.withName("Conceito X")
				.build();
		
		
		ResponseEntity<RetrieveConceptDTO> response = 
				restTemplate.postForEntity("/concepts", createConceptDto, RetrieveConceptDTO.class);
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	public void retrieveAll_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		RetrieveConceptDTO expectedConcept = new RetrieveConceptDTO(createdConcept);
	
		
		ResponseEntity<PagedModel<RetrieveConceptDTO>> response = restTemplate.exchange(
				"/concepts",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<PagedModel<RetrieveConceptDTO>>() {});
		PagedModel<RetrieveConceptDTO> resources = response.getBody();
		List<RetrieveConceptDTO> concepts = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(concepts).size().isEqualTo(1);
		assertThat(concepts).contains(expectedConcept);
	}
	
	@Test
	public void retrieveById_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		RetrieveConceptDTO expectedConcept = new RetrieveConceptDTO(createdConcept);
		String url = "/concepts/" + expectedConcept.getId();
		
		
		ResponseEntity<RetrieveConceptDTO> response = 
				restTemplate.getForEntity(url, RetrieveConceptDTO.class);
		RetrieveConceptDTO retrievedConceptDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedConceptDto).isEqualTo(expectedConcept);
	}
	
	@Test
	public void retrieveById_withoutConcept_shouldReturn404() {
		String url = "/concepts/" + 1555L;
		
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		String url = "/concepts/" + createdConcept.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<Concept> fetchedConcept = 
				this.conceptRepository.findById(createdConcept.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedConcept.isPresent()).isFalse();
	}
	
	@Test
	public void updateById_shouldWork() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		String url = "/concepts/" + createdConcept.getId();		
		HttpHeaders headers = new HttpHeaders();
		UpdateConceptDTO updateConceptDto = new UpdateConceptDTOBuilder()
				.withName("Conceito atualizado")
				.withDescription("Descrição atualizada")
				.build();
		HttpEntity<UpdateConceptDTO> httpEntity = 
				new HttpEntity<UpdateConceptDTO>(updateConceptDto, headers);
		
		
		ResponseEntity<RetrieveConceptDTO> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				httpEntity,
				RetrieveConceptDTO.class);
		RetrieveConceptDTO responseConcept = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseConcept.getName()).isEqualTo(updateConceptDto.getName());
		assertThat(responseConcept.getDescription()).isEqualTo(updateConceptDto.getDescription());
		assertThat(responseConcept.getId()).isEqualTo(createdConcept.getId());
		assertThat(responseConcept.getCreatedAt().isEqual(createdConcept.getCreatedAt())).isTrue();
	}
	
	@Test
	public void updateById_withoutName_shouldReturn400() {
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		String url = "/concepts/" + createdConcept.getId();		
		HttpHeaders headers = new HttpHeaders();
		UpdateConceptDTO updateConceptDto = new UpdateConceptDTOBuilder()
				.withDescription("Descrição atualizada")
				.build();
		HttpEntity<UpdateConceptDTO> httpEntity = new HttpEntity<UpdateConceptDTO>(updateConceptDto, headers);
		
		
		ResponseEntity<RetrieveConceptDTO> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				httpEntity,
				RetrieveConceptDTO.class);
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}

	private Concept buildAndSaveASimpleConcept() {
		Concept concept = new ConceptBuilder()
				.withName("Conceito X")
				.withDescription("O conceito X")
				.build();
		
		return this.conceptService.create(concept);
	}
}

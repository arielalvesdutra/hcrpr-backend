package dev.arielalvesdutra.hcrpr.it.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import dev.arielalvesdutra.hcrpr.builders.ProblemBuilder;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemConceptsDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemDTO;
import dev.arielalvesdutra.hcrpr.dto.builders.CreateProblemDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.UpdateProblemDTOBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;
import dev.arielalvesdutra.hcrpr.services.ConceptService;
import dev.arielalvesdutra.hcrpr.services.ProblemService;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProblemControllerIT {
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private ConceptRepository conceptRepository;
	
	
	@Autowired
	private ProblemCommentRepository problemCommentRepository;
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@After
	public void tearDown() {
		problemRepository.deleteAll();
		conceptRepository.deleteAll();
		problemCommentRepository.deleteAll();
	}	
	
	@Test
	public void create_shouldWorkAndReturn201() {
		CreateProblemDTO createProblemDto = new CreateProblemDTOBuilder()
				.withName("Técnica X")				
				.withDescription("A técnica X")
				.build();
		
		
		ResponseEntity<RetrieveProblemDTO> response = 
				restTemplate.postForEntity("/problems", createProblemDto, RetrieveProblemDTO.class);
		RetrieveProblemDTO retrievedProblemDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(retrievedProblemDto.getName()).isEqualTo(createProblemDto.getName());
		assertThat(retrievedProblemDto.getDescription()).isEqualTo(createProblemDto.getDescription());
		assertThat(retrievedProblemDto.getCreatedAt()).isNotNull();
		assertThat(retrievedProblemDto.getId()).isNotNull();
	}
	
	@Test
	public void retrieveAll_shouldWork() {
		
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		RetrieveProblemDTO expectedProblem = new RetrieveProblemDTO(createdProblem);
	
		ResponseEntity<PagedModel<RetrieveProblemDTO>> response = restTemplate.exchange(
				"/problems",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<PagedModel<RetrieveProblemDTO>>() {});
		PagedModel<RetrieveProblemDTO> resources = response.getBody();
		List<RetrieveProblemDTO> problems = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(problems).size().isEqualTo(1);
		assertThat(problems).contains(expectedProblem);
	}
	
	@Test
	public void retrieveById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		RetrieveProblemDTO expectedProblem = new RetrieveProblemDTO(createdProblem);
		String url = "/problems/" + expectedProblem.getId();
		
		ResponseEntity<RetrieveProblemDTO> response = 
				restTemplate.getForEntity(url, RetrieveProblemDTO.class);
		RetrieveProblemDTO retrievedProblemDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedProblemDto).isEqualTo(expectedProblem);
	}
	
	@Test
	public void retrieveById_withoutProblem_shouldReturn404() {
		String url = "/problems/" + 1555L;
		
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		String url = "/problems/" + createdProblem.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<Problem> fetchedProblem = 
				this.problemRepository.findById(createdProblem.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedProblem.isPresent()).isFalse();
	}
	
	@Test
	public void updateById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		String url = "/problems/" + createdProblem.getId();		
		HttpHeaders headers = new HttpHeaders();
		UpdateProblemDTO updateProblemDto = new UpdateProblemDTOBuilder()
				.withName("Problema atualizado")
				.withDescription("Descrição atualizada")
				.build();
		HttpEntity<UpdateProblemDTO> httpEntity = 
				new HttpEntity<UpdateProblemDTO>(updateProblemDto, headers);
		
		
		ResponseEntity<RetrieveProblemDTO> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				httpEntity,
				RetrieveProblemDTO.class);
		RetrieveProblemDTO responseProblem = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseProblem.getName()).isEqualTo(updateProblemDto.getName());
		assertThat(responseProblem.getDescription()).isEqualTo(updateProblemDto.getDescription());
		assertThat(responseProblem.getId()).isEqualTo(createdProblem.getId());
		assertThat(responseProblem.getCreatedAt().isEqual(createdProblem.getCreatedAt())).isTrue();
	}
	
	@Test
	public void updateProblemConcepts_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		RetrieveConceptDTO expectedConceptDto = new RetrieveConceptDTO(createdConcept);
		
		UpdateProblemConceptsDTO updateProblemConceptsDto = new UpdateProblemConceptsDTO();
		List<Long> conceptsIds = new ArrayList<Long>();
		conceptsIds.add(createdConcept.getId());
		updateProblemConceptsDto.setConceptsIds(conceptsIds);
			
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UpdateProblemConceptsDTO> httpEntity = 
				new HttpEntity<UpdateProblemConceptsDTO>(updateProblemConceptsDto, headers);
		
		String url = "/problems/" + createdProblem.getId() + "/concepts";
		
		ResponseEntity<List<RetrieveConceptDTO>> response = restTemplate.exchange(
						url,
						HttpMethod.PUT,
						httpEntity, 
						new ParameterizedTypeReference<List<RetrieveConceptDTO>>() {});
		List<RetrieveConceptDTO> conceptsDto = response.getBody();
		Problem updatedProblem = this.problemService.findById(createdProblem.getId());
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(conceptsDto).contains(expectedConceptDto);
		assertThat(updatedProblem.getRelatedConcepts()).contains(createdConcept);
	}
	
	@Test
	public void retrieveAllProblemConcepts_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		
		this.problemService.updateRelatedConcepts(
				createdProblem.getId(), 
				this.buildAConceptSetFromAConcept(createdConcept));
		
		RetrieveConceptDTO expectedConceptDto = new RetrieveConceptDTO(createdConcept);
		String url = "/problems/" + createdProblem.getId() + "/concepts";
		
		ResponseEntity<PagedModel<RetrieveConceptDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveConceptDTO>>() {});
		PagedModel<RetrieveConceptDTO> resources = response.getBody();
		List<RetrieveConceptDTO> retrievedConcepts = new ArrayList<>(resources.getContent());
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedConcepts).contains(expectedConceptDto);		
	}
	
	@Test
	public void createProblemComment_shouldWorkAndReturn201() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		CreateProblemCommentDTO createCommentDto = new CreateProblemCommentDTO("Comentário no problema");
		String url = "/problems/" + createdProblem.getId() + "/comments";
		
		
		ResponseEntity<RetrieveProblemCommentDTO> response = restTemplate.postForEntity(
				url, createCommentDto, RetrieveProblemCommentDTO.class);
		RetrieveProblemCommentDTO retrievedComment = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(retrievedComment.getId()).isNotNull();
		assertThat(retrievedComment.getCreatedAt()).isNotNull();
		assertThat(retrievedComment.getContent()).isEqualTo(createCommentDto.getContent());		
	}
	
	@Test
	public void findAllProblemComments_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		ProblemComment createdComment = this.buildAndSaveAProblemCommentWithAProblem(createdProblem);
		RetrieveProblemCommentDTO expectedCommentDto = new RetrieveProblemCommentDTO(createdComment);
		String url = "/problems/" + createdProblem.getId() + "/comments";
		
		
		ResponseEntity<PagedModel<RetrieveProblemCommentDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveProblemCommentDTO>>() {});
		PagedModel<RetrieveProblemCommentDTO> resources = response.getBody();
		List<RetrieveProblemCommentDTO> retrievedComments = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedComments).contains(expectedCommentDto);		
	}
	
	@Test
	public void deleteProblemComment_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		ProblemComment createdComment = this.buildAndSaveAProblemCommentWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + "/comments/" + createdComment.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<ProblemComment> fetchedProblemComment = 
				this.problemCommentRepository.findById(createdProblem.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedProblemComment.isPresent()).isFalse();
	}
	
	private Problem buildAndSaveASimpleProblem() {
		Problem problem = new ProblemBuilder()
				.withName("Problema X")
				.withDescription("O problema X")
				.build();
		
		return this.problemService.create(problem);
	}
	
	private Concept buildAndSaveASimpleConcept() {
		Concept concept = new ConceptBuilder()
				.withName("Conceito X")
				.withDescription("O conceito X")
				.build();
		
		return this.conceptService.create(concept);
	}
	
	private Set<Concept> buildAConceptSetFromAConcept(Concept concept) {
		Set<Concept> concepts = new HashSet<Concept>();
		concepts.add(concept);
		return concepts;
	}
	
	private ProblemComment buildAndSaveAProblemCommentWithAProblem(Problem problem) {
		ProblemComment comment = new ProblemComment("Comentário para o problema");
		comment.setProblem(problem);
		return this.problemCommentRepository.save(comment);
	}
}

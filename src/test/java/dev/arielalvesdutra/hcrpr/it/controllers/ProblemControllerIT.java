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
import dev.arielalvesdutra.hcrpr.builders.SolutionAttemptBuilder;
import dev.arielalvesdutra.hcrpr.builders.TechniqueBuilder;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateGoalDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateSolutionAttemptCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateSolutionAttemptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveGoalDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveSolutionAttemptCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveSolutionAttemptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveTechniqueDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateGoalDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemConceptsDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateSolutionAttemptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateSolutionAttemptTechniquesDTO;
import dev.arielalvesdutra.hcrpr.dto.builders.CreateProblemDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.CreateSolutionAttemptDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.UpdateGoalDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.UpdateProblemDTOBuilder;
import dev.arielalvesdutra.hcrpr.dto.builders.UpdateSolutionAttemptDTOBuilder;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;
import dev.arielalvesdutra.hcrpr.repositories.GoalRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.ProblemRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptCommentRepository;
import dev.arielalvesdutra.hcrpr.repositories.SolutionAttemptRepository;
import dev.arielalvesdutra.hcrpr.repositories.TechniqueRepository;
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
	private GoalRepository goalRepository;
	
	@Autowired
	private SolutionAttemptRepository solutionAttemptRepository;
	
	@Autowired
	private TechniqueRepository techniqueRepository;
	
	@Autowired
	private SolutionAttemptCommentRepository solutionAttemptCommentRepository;
	
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
		goalRepository.deleteAll();
		solutionAttemptRepository.deleteAll();
		techniqueRepository.deleteAll();
		solutionAttemptCommentRepository.deleteAll();
	}	
	
	@Test
	public void create_shouldWorkAndReturn201() {
		CreateProblemDTO createProblemDto = new CreateProblemDTOBuilder()
				.withName("Problema X")				
				.withDescription("O problem X...")
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
		Optional<Problem> fetchedProblem = this.problemRepository.findById(createdProblem.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedProblem.isPresent()).isFalse();
	}
	
	@Test
	public void updateById_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		String url = "/problems/" + createdProblem.getId();		
		UpdateProblemDTO updateProblemDto = new UpdateProblemDTOBuilder()
				.withName("Problema atualizado")
				.withDescription("Descrição atualizada")
				.build();
		HttpHeaders headers = new HttpHeaders();
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
		assertThat(responseProblem.getCreatedAt().isEqual(createdProblem.getCreatedAt()));
	}
	
	@Test
	public void updateProblemConcepts_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Concept createdConcept = this.buildAndSaveASimpleConcept();
		RetrieveConceptDTO expectedConceptDto = new RetrieveConceptDTO(createdConcept);
		UpdateProblemConceptsDTO updateProblemConceptsDto = 
				this.buildUpdateProblemConceptsDTOWithConcept(createdConcept);
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
	public void retrieveAllProblemComments_shouldWork() {
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
		Optional<ProblemComment> fetchedComment = 
				this.problemCommentRepository.findById(createdComment.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedComment.isPresent()).isFalse();
	}
	
	@Test
	public void createProblemGoal_shouldWork() { 
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		CreateGoalDTO createGoalDto = new CreateGoalDTO("Objetivo B");
		String url = "/problems/" + createdProblem.getId() + "/goals";
		
		
		ResponseEntity<RetrieveGoalDTO> response = restTemplate.postForEntity(
				url, createGoalDto, RetrieveGoalDTO.class);
		RetrieveGoalDTO retrievedGoal = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(retrievedGoal.getId()).isNotNull();
		assertThat(retrievedGoal.getCreatedAt()).isNotNull();
		assertThat(retrievedGoal.getDescription()).isEqualTo(createGoalDto.getDescription());		
	}
	
	@Test
	public void retrieveAllProblemGoals_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveAGoalWithAProblem(createdProblem);
		RetrieveGoalDTO expectedGoalDto = new RetrieveGoalDTO(createdGoal);
		String url = "/problems/" + createdProblem.getId() + "/goals";
		
		
		ResponseEntity<PagedModel<RetrieveGoalDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveGoalDTO>>() {});
		PagedModel<RetrieveGoalDTO> resources = response.getBody();
		List<RetrieveGoalDTO> retrievedGoals = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedGoals).contains(expectedGoalDto);		
	}
	
	@Test
	public void deleteProblemGoalByProblemIdAndGoalId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveAGoalWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + "/goals/" + createdGoal.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<Goal> fetchedGoal = this.goalRepository.findById(createdGoal.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedGoal.isPresent()).isFalse();
	}
	
	@Test
	public void updateProblemGoalByGoalId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		Goal createdGoal = this.buildAndSaveAGoalWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + "/goals/" + createdGoal.getId();
		UpdateGoalDTO updateGoalDto = new UpdateGoalDTOBuilder()
				.withDescription("Descrição modificada")
				.withAchieved(true)
				.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UpdateGoalDTO> httpEntity = new HttpEntity<UpdateGoalDTO>(updateGoalDto, headers);
	
		
		ResponseEntity<RetrieveGoalDTO> response = restTemplate.exchange(
						url,
						HttpMethod.PUT,
						httpEntity, 
						RetrieveGoalDTO.class);
		RetrieveGoalDTO responseGoal = response.getBody();
		Goal updatedGoal = this.goalRepository.findById(createdGoal.getId()).get();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseGoal.getAchieved()).isEqualTo(updateGoalDto.isAchieved());
		assertThat(responseGoal.getDescription()).isEqualTo(updateGoalDto.getDescription());
		assertThat(updatedGoal.getProblem()).isEqualTo(createdProblem);
	}
	
	@Test
	public void createProblemSolutionAttempt_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		CreateSolutionAttemptDTO createAttemptDto = new CreateSolutionAttemptDTOBuilder()
				.withName("Tentativa M")
				.withDescription("A tentativa M...")
				.build();
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts";
		
		
		ResponseEntity<RetrieveSolutionAttemptDTO> response = restTemplate.postForEntity(
				url, createAttemptDto, RetrieveSolutionAttemptDTO.class);
		RetrieveSolutionAttemptDTO responseSoluttionAttempt = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(responseSoluttionAttempt.getId()).isNotNull();
		assertThat(responseSoluttionAttempt.getCreatedAt()).isNotNull();
		assertThat(responseSoluttionAttempt.getName()).isEqualTo(createAttemptDto.getName());
		assertThat(responseSoluttionAttempt.getDescription()).isEqualTo(createAttemptDto.getDescription());
	}
	
	@Test
	public void retrieveAllProblemSolutionAttempts_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = 
				this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		RetrieveSolutionAttemptDTO expectedAttemptDto = new RetrieveSolutionAttemptDTO(createdAttempt);
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts";
		
		
		ResponseEntity<PagedModel<RetrieveSolutionAttemptDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveSolutionAttemptDTO>>() {});
		PagedModel<RetrieveSolutionAttemptDTO> resources = response.getBody();
		List<RetrieveSolutionAttemptDTO> retrievedAttempts = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedAttempts).contains(expectedAttemptDto);		
	}
	
	@Test
	public void retrieveProblemSolutionAttemptByProbleIdAndAttemptId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = 
				this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		RetrieveSolutionAttemptDTO expectedAttemptDto = 
				new RetrieveSolutionAttemptDTO(createdAttempt);
		String url = "/problems/" + createdProblem.getId() +
				"/solution-attempts/" + createdAttempt.getId() ;

		
		ResponseEntity<RetrieveSolutionAttemptDTO> response = 
				restTemplate.getForEntity(url, RetrieveSolutionAttemptDTO.class);
		RetrieveSolutionAttemptDTO retrievedAttemptDto = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedAttemptDto).isEqualTo(expectedAttemptDto);
	}
	
	@Test
	public void updateProblemSolutionAttemptByAttemptId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts/" + createdAttempt.getId();
		UpdateSolutionAttemptDTO updateAttemptDto = new UpdateSolutionAttemptDTOBuilder()
				.withName("Tentiva atualizada")
				.withDescription("Descrição atualizada")
				.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UpdateSolutionAttemptDTO> httpEntity = 
				new HttpEntity<UpdateSolutionAttemptDTO>(updateAttemptDto, headers);
	
		
		ResponseEntity<RetrieveSolutionAttemptDTO> response = restTemplate.exchange(
						url,
						HttpMethod.PUT,
						httpEntity, 
						RetrieveSolutionAttemptDTO.class);
		RetrieveSolutionAttemptDTO responseAttempt = response.getBody();
		SolutionAttempt updatedAttempt = 
				this.solutionAttemptRepository.findById(createdAttempt.getId()).get();
		RetrieveSolutionAttemptDTO expectedAttempt = 
				new RetrieveSolutionAttemptDTO(updatedAttempt);
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(responseAttempt).isEqualTo(expectedAttempt);
		assertThat(responseAttempt.getName()).isEqualTo(updateAttemptDto.getName());
		assertThat(responseAttempt.getDescription()).isEqualTo(updateAttemptDto.getDescription());
		assertThat(updatedAttempt.getProblem()).isEqualTo(createdProblem);
	}
	
	@Test
	public void deleteProblemSolutionAttemptsByProblemIdAndAttemptId_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = 
				this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + 
				"/solution-attempts/" + createdAttempt.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<SolutionAttempt> fetchedAttempt = 
				this.solutionAttemptRepository.findById(createdAttempt.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedAttempt.isPresent()).isFalse();
	}

	@Test
	public void updateProblemSolutionAttemptTechniques_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		Technique createdTechnique = this.buildAndSaveATechnique();
		RetrieveTechniqueDTO expectedTechnique = new RetrieveTechniqueDTO(createdTechnique);
		UpdateSolutionAttemptTechniquesDTO updateAttemptTechniquesDto = 
				this.buildAUpdateSolutionAttemptTechniquesDTOWithTechnique(createdTechnique);		
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts/" + createdAttempt.getId() + "/techniques";
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UpdateSolutionAttemptTechniquesDTO> httpEntity = new HttpEntity<UpdateSolutionAttemptTechniquesDTO>(updateAttemptTechniquesDto, headers);
		
		
		ResponseEntity<PagedModel<RetrieveTechniqueDTO>> response = restTemplate.exchange(
						url,
						HttpMethod.PUT,
						httpEntity, 
						new ParameterizedTypeReference<PagedModel<RetrieveTechniqueDTO>>() {});
		PagedModel<RetrieveTechniqueDTO> techniquesPage = response.getBody();
		SolutionAttempt updatedAttempt = 
				this.problemService.findSolutionAttempt(createdProblem.getId(), createdAttempt.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(techniquesPage.getContent()).contains(expectedTechnique);
		assertThat(updatedAttempt.getTechniques()).contains(createdTechnique);
	}	

	@Test
	public void retrieveAllProblemSolutionAttemptTechniques_shouldWork() {
		Technique createdTechnique = this.buildAndSaveATechnique();
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblemAndATechnique(
				createdProblem, createdTechnique);
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts/" + createdAttempt.getId() + "/techniques";
		RetrieveTechniqueDTO expectedTechniqueDto =  new RetrieveTechniqueDTO(createdTechnique);
		
		
		ResponseEntity<PagedModel<RetrieveTechniqueDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveTechniqueDTO>>() {});
		PagedModel<RetrieveTechniqueDTO> resources = response.getBody();
		List<RetrieveTechniqueDTO> retrievedTechniques = new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedTechniques).contains(expectedTechniqueDto);
	}
	
	@Test
	public void createProblemSolutionAttemptComment_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts/" + createdAttempt.getId() + "/comments";
		CreateSolutionAttemptCommentDTO createCommentDto = new CreateSolutionAttemptCommentDTO("Comentário para a tentativa");
		
		
		ResponseEntity<RetrieveSolutionAttemptCommentDTO> response = restTemplate.postForEntity(
				url, createCommentDto, RetrieveSolutionAttemptCommentDTO.class);
		RetrieveSolutionAttemptCommentDTO responseComment = response.getBody();
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().containsKey("Location")).isTrue();
		assertThat(responseComment.getId()).isNotNull();
		assertThat(responseComment.getCreatedAt()).isNotNull();
		assertThat(responseComment.getContent()).isEqualTo(createCommentDto.getContent());	
	}
	
	@Test
	public void retrieveAllProblemSolutionAttemptComment_shouldWork() {		
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		SolutionAttemptComment createdComment =
				this.buildAndSaveASolutionAttemptCommentWithAAttempt(createdAttempt);
		String url = "/problems/" + createdProblem.getId() + "/solution-attempts/" + createdAttempt.getId() + "/comments";
		RetrieveSolutionAttemptCommentDTO expectedCommentDto = 
				new RetrieveSolutionAttemptCommentDTO(createdComment);
		
		
		ResponseEntity<PagedModel<RetrieveSolutionAttemptCommentDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET, 
				null,				
				new ParameterizedTypeReference<PagedModel<RetrieveSolutionAttemptCommentDTO>>() {});
		PagedModel<RetrieveSolutionAttemptCommentDTO> resources = response.getBody();
		List<RetrieveSolutionAttemptCommentDTO> retrievedComments = 
				new ArrayList<>(resources.getContent());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(retrievedComments).contains(expectedCommentDto);
	}
	
	@Test
	public void deleteProblemSolutionAttemptComment_shouldWork() {
		Problem createdProblem = this.buildAndSaveASimpleProblem();
		SolutionAttempt createdAttempt = this.buildAndSaveASolutionAttemptWithAProblem(createdProblem);
		SolutionAttemptComment createdComment =
				this.buildAndSaveASolutionAttemptCommentWithAAttempt(createdAttempt);
		String url = "/problems/" + createdProblem.getId() + 
				"/solution-attempts/" + createdAttempt.getId() + 
				"/comments/" + createdComment.getId();
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.DELETE,
				null,
				String.class);
		Optional<SolutionAttemptComment> fetchedComment = 
				this.solutionAttemptCommentRepository.findById(createdComment.getId());
		
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(fetchedComment.isPresent()).isFalse();
	}	
	
	private SolutionAttemptComment buildAndSaveASolutionAttemptCommentWithAAttempt(SolutionAttempt attempt) {
		SolutionAttemptComment comment = new SolutionAttemptComment("Comentário para a tentativa");
		comment.setSolutionAttempt(attempt);
		return this.solutionAttemptCommentRepository.save(comment);
	}
	
	private SolutionAttempt buildAndSaveASolutionAttemptWithAProblemAndATechnique(
			Problem problem, Technique technique) {
		Set<Technique> techniques = new HashSet<Technique>();
		techniques.add(technique);
		
		SolutionAttempt soluttionAttempt = this.buildASimpleSulutionAttempt();	
		soluttionAttempt.setProblem(problem);	
		soluttionAttempt.setTechniques(techniques);
		return this.solutionAttemptRepository.save(soluttionAttempt);
	}
	
	private Technique buildAndSaveATechnique() {
		Technique technique = new TechniqueBuilder()
				.withName("Tecnica X")
				.withDescription("A técnica X")
				.build();
		return this.techniqueRepository.save(technique);
	}
	
	private SolutionAttempt buildASimpleSulutionAttempt() {
		return new SolutionAttemptBuilder()
				.withName("Utilizando a técnica Z")
				.withDescription("A tentativa do solução com a técnica...")
				.build();
	}
	
	private SolutionAttempt buildAndSaveASolutionAttemptWithAProblem(Problem problem) {
		SolutionAttempt soluttionAttempt = this.buildASimpleSulutionAttempt();	
		soluttionAttempt.setProblem(problem);	
		return this.solutionAttemptRepository.save(soluttionAttempt);
	}

	private Goal buildAndSaveAGoalWithAProblem(Problem problem) {
		Goal createGoal = new Goal("Objetivo B");
		createGoal.setProblem(problem);
		return this.goalRepository.save(createGoal);
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
	
	private UpdateProblemConceptsDTO buildUpdateProblemConceptsDTOWithConcept(Concept concept) {
		UpdateProblemConceptsDTO updateProblemConceptsDto = new UpdateProblemConceptsDTO();
		List<Long> conceptsIds = new ArrayList<Long>();
		conceptsIds.add(concept.getId());
		updateProblemConceptsDto.setConceptsIds(conceptsIds);
		
		return updateProblemConceptsDto;
	}
	
	private UpdateSolutionAttemptTechniquesDTO buildAUpdateSolutionAttemptTechniquesDTOWithTechnique(
			Technique technique) {
		
		UpdateSolutionAttemptTechniquesDTO updateAttemptTechniquesDto = 
				new UpdateSolutionAttemptTechniquesDTO();
		List<Long> techniquesIds = new ArrayList<Long>();
		techniquesIds.add(technique.getId());
		updateAttemptTechniquesDto.setTechniquesIds(techniquesIds);
		
		return updateAttemptTechniquesDto;
	}
}

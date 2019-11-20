package dev.arielalvesdutra.hcrpr.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.services.ConceptService;
import dev.arielalvesdutra.hcrpr.services.ProblemService;
import dev.arielalvesdutra.hcrpr.services.TechniqueService;

@RequestMapping("/problems")
@RestController
public class ProblemController {

	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private TechniqueService techniqueService;
	
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<RetrieveProblemDTO> create(
			@Valid @RequestBody CreateProblemDTO createProblemDto,
			UriComponentsBuilder uriBuilder) {
		
		Problem createdProblem = this.problemService.create(createProblemDto.toProblem());		
		URI uri = uriBuilder.path("/problems/{id}")
							.buildAndExpand(createdProblem.getId())
							.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveProblemDTO(createdProblem));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<RetrieveProblemDTO>> retrieveAll(
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
		Page<Problem> problemsPage = this.problemService.findAll(pagination);
		
		return ResponseEntity.ok().body(
				RetrieveProblemDTO.fromProblemPageToRetrieveProblemDTOPage(problemsPage));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{problemId}")
	public ResponseEntity<RetrieveProblemDTO> retrieveById(@PathVariable Long problemId) {
		
		Problem problem = this.problemService.findById(problemId);
		
		return ResponseEntity.ok().body(new RetrieveProblemDTO(problem));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/{problemId}")
	public ResponseEntity<?> deleteById(@PathVariable Long problemId) {
	
		this.problemService.deleteById(problemId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{problemId}")
	public ResponseEntity<RetrieveProblemDTO> updateById(
			@PathVariable Long problemId,
			@Valid @RequestBody UpdateProblemDTO updateProblemDto) {
		
		Problem updatedProblem = this.problemService.update(problemId, updateProblemDto.toProblem());
		
		return ResponseEntity.ok().body(new RetrieveProblemDTO(updatedProblem));
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{problemId}/concepts")
	public ResponseEntity<List<RetrieveConceptDTO>> updateRelatedConcepts(
			@PathVariable Long problemId,
			@RequestBody UpdateProblemConceptsDTO updateProblemConceptsDto) {
		
		List<Concept> foundConcepts = 
				this.conceptService.findByIds(updateProblemConceptsDto.getConceptsIds());
		Set<Concept> problemConcepts = 
				this.problemService.updateRelatedConcepts(problemId, new HashSet<Concept>(foundConcepts));
		List<RetrieveConceptDTO> retrieveConceptDtoList = 
				RetrieveConceptDTO.fromConceptSetToRetrieveConceptDTOList(problemConcepts);
		
		return ResponseEntity.ok().body(retrieveConceptDtoList);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{problemId}/concepts")
	public ResponseEntity<Page<RetrieveConceptDTO>> retrieveAllRelatedConcepts(
			@PathVariable Long problemId,
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
		Page<Concept> conceptsPage = this.problemService.findAllRelatedConcepts(problemId, pagination);
		
		return ResponseEntity.ok().body(
				RetrieveConceptDTO.fromConceptPageToRetrieveConceptDTOPage(conceptsPage));
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/{problemId}/comments")
	public ResponseEntity<RetrieveProblemCommentDTO> createProblemComment(
			@PathVariable Long problemId,
			@Valid @RequestBody CreateProblemCommentDTO createCommentDto,
			UriComponentsBuilder uriBuilder) {
		
		ProblemComment createdComment = this.problemService.createProblemComment(
				problemId, createCommentDto.toProblemComment());
		Map<String, Long> pathParams = new HashMap<>();
		pathParams.put("problemId", problemId);
		pathParams.put("commentId", createdComment.getId());	
		URI uri = uriBuilder.path("/problems/{problemId}/comments/{commentId}")
				.buildAndExpand(pathParams)
				.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveProblemCommentDTO(createdComment));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{problemId}/comments")
	public ResponseEntity<Page<RetrieveProblemCommentDTO>> retrieveAllProblemComments(
			@PathVariable Long problemId,
			@PageableDefault(sort="content", page = 0, size = 10) Pageable pagination) {
		
		Page<ProblemComment> commentsPage = this.problemService.findAllProblemComments(problemId, pagination);
		
		return ResponseEntity.ok().body(
				RetrieveProblemCommentDTO.fromCommentPageToRetrieveProblemCommentDTOPage(commentsPage));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/{problemId}/comments/{commentId}")
	public ResponseEntity<?> deleteProblemCommentByCommentId(
			@PathVariable Long problemId,
			@PathVariable Long commentId) {
	
		this.problemService.deleteProblemComment(problemId, commentId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/{problemId}/goals")
	public ResponseEntity<RetrieveGoalDTO> createGoal(
			@PathVariable Long problemId,
			@Valid @RequestBody CreateGoalDTO createGoalDto,
			UriComponentsBuilder uriBuilder) {
		
		Goal createdGoal = this.problemService.createGoal(
				problemId, createGoalDto.toGoal());
		Map<String, Long> pathParams = new HashMap<>();
		pathParams.put("problemId", problemId);
		pathParams.put("goalId", createdGoal.getId());	
		URI uri = uriBuilder.path("/problems/{problemId}/goals/{goalId}")
				.buildAndExpand(pathParams)
				.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveGoalDTO(createdGoal));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{problemId}/goals")
	public ResponseEntity<Page<RetrieveGoalDTO>> retrieveAllGoals(
			@PathVariable Long problemId,
			@PageableDefault(sort="description", page = 0, size = 10) Pageable pagination) {
		
		Page<Goal> goalsPage = this.problemService.findAllGoals(problemId, pagination);
		
		return ResponseEntity.ok().body(
				RetrieveGoalDTO.fromGoalPageToRetrieveGoalDTOPage(goalsPage));
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{problemId}/goals/{goalId}")
	public ResponseEntity<?> deleteGoalByProblemIdAndGoalId(
			@PathVariable Long problemId,
			@PathVariable Long goalId) {
	
		this.problemService.deleteGoal(problemId, goalId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{problemId}/goals/{goalId}")
	public ResponseEntity<RetrieveGoalDTO> updateGoalByProblemIdAndGoalId(
			@PathVariable Long problemId,
			@PathVariable Long goalId,
			@Valid @RequestBody UpdateGoalDTO updateGoalDto) {
	
		Goal updatedGoal = this.problemService.updateGoal(problemId, goalId, updateGoalDto.toGoal());
		
		return ResponseEntity.ok().body(new RetrieveGoalDTO(updatedGoal));
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/{problemId}/solution-attempts")
	public ResponseEntity<RetrieveSolutionAttemptDTO> createSolutionAttempt(
			@PathVariable Long problemId,
			@Valid @RequestBody CreateSolutionAttemptDTO createSolutionAttemptDto,
			UriComponentsBuilder uriBuilder) {
		
		SolutionAttempt createdSolutionAttempt = this.problemService.createSolutionAttempt(
				problemId, createSolutionAttemptDto.toSolutionAttempt());
		Map<String, Long> pathParams = new HashMap<>();
		pathParams.put("problemId", problemId);
		pathParams.put("solutionAttemptId", createdSolutionAttempt.getId());	
		URI uri = uriBuilder.path("/problems/{problemId}/solution-attempts/{solutionAttemptId}")
				.buildAndExpand(pathParams)
				.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveSolutionAttemptDTO(createdSolutionAttempt));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{problemId}/solution-attempts")
	public ResponseEntity<Page<RetrieveSolutionAttemptDTO>> retrieveAllSolutionAttempts(
			@PathVariable Long problemId,
			@PageableDefault(sort="description", page = 0, size = 10) Pageable pagination) {
		
		Page<SolutionAttempt> attemptsPage = 
				this.problemService.findAllSolutionAttempts(problemId, pagination);
		
		return ResponseEntity.ok().body(
				RetrieveSolutionAttemptDTO.fromSolutionAttemptPageToRetrieveSolutionAttemptDTOPage(attemptsPage));
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			path = "/{problemId}/solution-attempts/{solutionAttemptId}")
	public ResponseEntity<RetrieveSolutionAttemptDTO> retrieveSolutionAttemptByProblemIdAndAttemptId(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId) {
		
		SolutionAttempt attempt = this.problemService.findSolutionAttempt(problemId, solutionAttemptId);

		return ResponseEntity.ok().body(new RetrieveSolutionAttemptDTO(attempt));
	}
	
	@RequestMapping(method = RequestMethod.PUT, 
			path = "/{problemId}/solution-attempts/{sulutionAttemptId}")
	public ResponseEntity<RetrieveSolutionAttemptDTO> updateSolutionAttemptByProblemIdAndAttemptId(
			@PathVariable Long problemId,
			@PathVariable Long sulutionAttemptId,
			@Valid @RequestBody UpdateSolutionAttemptDTO updateAttemptDto) {
	
		SolutionAttempt updatedAttempt = this.problemService.updateSolutionAttempt(
				problemId, sulutionAttemptId, updateAttemptDto.toSolutionAttempt());
		
		return ResponseEntity.ok().body(new RetrieveSolutionAttemptDTO(updatedAttempt));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, 
			path = "/{problemId}/solution-attempts/{solutionAttemptId}")
	public ResponseEntity<?> deleteSolutionAttemptByProblemIdAndAttemptId(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId) {
	
		this.problemService.deleteSolutionAttempt(problemId, solutionAttemptId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.PUT, 
			path = "/{problemId}/solution-attempts/{solutionAttemptId}/techniques")
	public ResponseEntity<Page<RetrieveTechniqueDTO>> updateSolutionAttemptTechniques(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId,
			@RequestBody UpdateSolutionAttemptTechniquesDTO updateDto) {
		
		List<Technique> techniques = 
				this.techniqueService.findByIds(updateDto.getTechniquesIds());

		Set<Technique> attemptTechniques = this.problemService.updateSolutionAttemptTechniques(
				problemId, solutionAttemptId, new HashSet<Technique>(techniques));
		
		return ResponseEntity.ok().body(
				RetrieveTechniqueDTO.fromTechniqueSetToRetrieveTechniqueDTOPage(attemptTechniques));
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "/{problemId}/solution-attempts/{solutionAttemptId}/techniques")
	public ResponseEntity<Page<RetrieveTechniqueDTO>> retrieveAllSolutionAttemptTechniques(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId,
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
		Page<Technique> techniquesPage = 
				this.problemService.findAllSolutionAttemptTechniques(problemId, solutionAttemptId, pagination);
		
		return ResponseEntity.ok().body(
				RetrieveTechniqueDTO.fromTechniquePageToRetrieveTechniqueDTOPage(techniquesPage));
	}

	@RequestMapping(method = RequestMethod.POST, 
			path = "/{problemId}/solution-attempts/{solutionAttemptId}/comments")
	public ResponseEntity<RetrieveSolutionAttemptCommentDTO> createSolutionAttemptComment(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId,
			@Valid @RequestBody CreateSolutionAttemptCommentDTO createCommentDto,
			UriComponentsBuilder uriBuilder) {
		
		SolutionAttemptComment createdComment = this.problemService.createSolutionAttemptComment(
				problemId, solutionAttemptId, createCommentDto.toSolutionAttemptComment());
		
		Map<String, Long> pathParams = new HashMap<>();
		pathParams.put("problemId", problemId);
		pathParams.put("solutionAttemptId", solutionAttemptId);
		pathParams.put("commentId", createdComment.getId());	
		URI uri = uriBuilder.path("/problems/{problemId}/solution-attempts/{solutionAttemptId}/comments/{commentId}")
				.buildAndExpand(pathParams)
				.toUri();
		
		return ResponseEntity.created(uri).body(
				new RetrieveSolutionAttemptCommentDTO(createdComment));
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "/{problemId}/solution-attempts/{solutionAttemptId}/comments")
	public ResponseEntity<Page<RetrieveSolutionAttemptCommentDTO>> retrieveAllSolutionAttemptComments(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId,
			@PageableDefault(sort="content", page = 0, size = 10) Pageable pagination) {
		
		Page<SolutionAttemptComment> commentsPage = 
				this.problemService.findAllSolutionAttemptComments(problemId, solutionAttemptId, pagination);
		
		return ResponseEntity.ok().body(RetrieveSolutionAttemptCommentDTO
				.fromCommentPageToRetrieveSolutionAttemptCommentDTOPage(commentsPage));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, 
			path = "/{problemId}/solution-attempts/{solutionAttemptId}/comments/{commentId}")
	public ResponseEntity<?> deleteSolutionAttemptCommentByProblemIdAndAttemptIdAndCommentId(
			@PathVariable Long problemId,
			@PathVariable Long solutionAttemptId,
			@PathVariable Long commentId) {
	
		this.problemService.deleteSolutionAttemptComment(problemId, solutionAttemptId, commentId);
		
		return ResponseEntity.ok().build();
	}
	
}

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

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemCommentDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveProblemDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemConceptsDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemDTO;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.services.ConceptService;
import dev.arielalvesdutra.hcrpr.services.ProblemService;

@RequestMapping("/problems")
@RestController
public class ProblemController {

	@Autowired
	private ProblemService problemService;
	
	
	@Autowired
	private ConceptService conceptService;
	
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
			@RequestBody UpdateProblemDTO updateProblemDto) {
		
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
			@RequestBody CreateProblemCommentDTO createCommentDto,
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
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
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
}

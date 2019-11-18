package dev.arielalvesdutra.hcrpr.controllers;

import java.net.URI;

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

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveConceptDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateConceptDTO;
import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.services.ConceptService;

@RequestMapping("/concepts")
@RestController
public class ConceptController {
	
	@Autowired
	private ConceptService conceptService;
	
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<RetrieveConceptDTO> create(
			@Valid @RequestBody CreateConceptDTO createConceptDto,
			UriComponentsBuilder uriBuilder) {
		
		Concept createdConcept = this.conceptService.create(createConceptDto.toConcept());		
		URI uri = uriBuilder.path("/concepts/{id}")
							.buildAndExpand(createdConcept.getId())
							.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveConceptDTO(createdConcept));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<RetrieveConceptDTO>> retrieveAll(
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
		Page<Concept> conceptPage = this.conceptService.findAll(pagination);
		
		return ResponseEntity.ok().body(
				RetrieveConceptDTO.fromConceptPageToRetrieveConceptDTOPage(conceptPage));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{conceptId}")
	public ResponseEntity<RetrieveConceptDTO> retrieveById(@PathVariable Long conceptId) {
		
		Concept concept = this.conceptService.findById(conceptId);
		
		return ResponseEntity.ok().body(new RetrieveConceptDTO(concept));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/{conceptId}")
	public ResponseEntity<?> deleteById(@PathVariable Long conceptId) {
	
		this.conceptService.deleteById(conceptId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{conceptId}")
	public ResponseEntity<RetrieveConceptDTO> updateById(
			@PathVariable Long conceptId,
			@RequestBody UpdateConceptDTO updateConceptDto) {
		
		Concept updatedConcept = this.conceptService.update(conceptId, updateConceptDto.toConcept());
		
		return ResponseEntity.ok().body(new RetrieveConceptDTO(updatedConcept));
	}

}

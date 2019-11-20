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

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateTechniqueDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.RetrieveTechniqueDTO;
import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateTechniqueDTO;
import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.services.TechniqueService;

@RequestMapping("/techniques")
@RestController
public class TechniqueController {

	@Autowired
	private TechniqueService techniqueService;
	
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<RetrieveTechniqueDTO> create(
			@Valid @RequestBody CreateTechniqueDTO createTechniqueDto,
			UriComponentsBuilder uriBuilder) {
		
		Technique createdTechnique = this.techniqueService.create(createTechniqueDto.toTechnique());		
		URI uri = uriBuilder.path("/techniques/{id}")
							.buildAndExpand(createdTechnique.getId())
							.toUri();
		
		return ResponseEntity.created(uri).body(new RetrieveTechniqueDTO(createdTechnique));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<RetrieveTechniqueDTO>> retrieveAll(
			@PageableDefault(sort="name", page = 0, size = 10) Pageable pagination) {
		
		Page<Technique> techniquesPage = this.techniqueService.findAll(pagination);
		
		return ResponseEntity.ok().body(
				RetrieveTechniqueDTO.fromTechniquePageToRetrieveTechniqueDTOPage(techniquesPage));
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{techniqueId}")
	public ResponseEntity<RetrieveTechniqueDTO> retrieveById(@PathVariable Long techniqueId) {
		
		Technique technique = this.techniqueService.findById(techniqueId);
		
		return ResponseEntity.ok().body(new RetrieveTechniqueDTO(technique));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/{techniqueId}")
	public ResponseEntity<?> deleteById(@PathVariable Long techniqueId) {
	
		this.techniqueService.deleteById(techniqueId);
		
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{techniqueId}")
	public ResponseEntity<RetrieveTechniqueDTO> updateById(
			@PathVariable Long techniqueId,
			@Valid @RequestBody UpdateTechniqueDTO updateTechniqueDto) {
		
		Technique updatedTechnique = this.techniqueService.update(techniqueId, updateTechniqueDto.toTechnique());
		
		return ResponseEntity.ok().body(new RetrieveTechniqueDTO(updatedTechnique));
	}
}

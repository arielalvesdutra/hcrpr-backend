package dev.arielalvesdutra.hcrpr.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.Concept;

public class CreateConceptDTO {
	
	@NotEmpty
	@Size(min = 2)
	private String name;
	
	@NotEmpty
	@Size(min = 5)
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Concept toConcept() { 
		Concept concept = new Concept();
		concept.setName(this.getName());
		concept.setDescription(this.getDescription());
		
		return concept;
	}
}

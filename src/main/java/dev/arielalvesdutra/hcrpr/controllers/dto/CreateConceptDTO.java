package dev.arielalvesdutra.hcrpr.controllers.dto;

import dev.arielalvesdutra.hcrpr.entities.Concept;

public class CreateConceptDTO {
	private String name;
	
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

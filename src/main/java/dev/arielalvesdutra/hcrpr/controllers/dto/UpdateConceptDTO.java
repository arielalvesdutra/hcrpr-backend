package dev.arielalvesdutra.hcrpr.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.Concept;

public class UpdateConceptDTO {

	@NotEmpty
	@Size(min = 2)
	private String name;
	
	private String description;
	
	public UpdateConceptDTO() { }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Concept toConcept() {
		Concept concept = new Concept();
		concept.setName(this.getName());
		concept.setDescription(this.getDescription());
		
		return concept;
	}

	@Override
	public String toString() {
		return "UpdateConceptDTO [name=" + name + ", description=" + description + "]";
	}
}

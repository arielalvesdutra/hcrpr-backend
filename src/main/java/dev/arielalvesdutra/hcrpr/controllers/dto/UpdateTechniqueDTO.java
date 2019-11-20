package dev.arielalvesdutra.hcrpr.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.Technique;

public class UpdateTechniqueDTO {

	@NotEmpty
	@Size(min = 2)
	private String name;
	
	private String description;
	
	public UpdateTechniqueDTO() { }

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

	public Technique toTechnique() {
		Technique technique = new Technique();
		technique.setName(this.getName());
		technique.setDescription(this.getDescription());
		
		return technique;
	}

	@Override
	public String toString() {
		return "UpdateTechniqueDTO [name=" + name + ", description=" + description + "]";
	}
}

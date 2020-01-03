package dev.arielalvesdutra.hcrpr.builders.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateTechniqueDTO;

public class CreateTechniqueDTOBuilder {
	private CreateTechniqueDTO techniqueDto = new CreateTechniqueDTO();
	
	public CreateTechniqueDTOBuilder withName(String name) {
		this.techniqueDto.setName(name);
		return this;
	}
	
	public CreateTechniqueDTOBuilder withDescription(String description) {
		this.techniqueDto.setDescription(description);
		return this;
	}
	
	public CreateTechniqueDTO build() {
		return this.techniqueDto;
	}	
}

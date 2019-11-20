package dev.arielalvesdutra.hcrpr.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateTechniqueDTO;

public class UpdateTechniqueDTOBuilder {
	private UpdateTechniqueDTO techniqueDto = new UpdateTechniqueDTO();
	
	public UpdateTechniqueDTOBuilder withName(String name) {
		this.techniqueDto.setName(name);
		return this;
	}
	
	public UpdateTechniqueDTOBuilder withDescription(String description) {
		this.techniqueDto.setDescription(description);
		return this;
	}
	
	public UpdateTechniqueDTO build() {
		return this.techniqueDto;
	}	
}

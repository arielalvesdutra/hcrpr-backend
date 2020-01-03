package dev.arielalvesdutra.hcrpr.builders.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateConceptDTO;

public class CreateConceptDTOBuilder {
	private CreateConceptDTO createConceptDto = new CreateConceptDTO();
	
	public CreateConceptDTOBuilder withName(String name) {
		this.createConceptDto.setName(name);
		return this;
	}	
	
	public CreateConceptDTOBuilder withDescription(String description) {
		this.createConceptDto.setDescription(description);
		return this;
	}
	
	public CreateConceptDTO build() {
		return this.createConceptDto;
	}
}

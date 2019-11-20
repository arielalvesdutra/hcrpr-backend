package dev.arielalvesdutra.hcrpr.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateProblemDTO;

public class CreateProblemDTOBuilder {
	private CreateProblemDTO problemDto = new CreateProblemDTO();
	
	public CreateProblemDTOBuilder withName(String name) {
		this.problemDto.setName(name);
		return this;
	}
	
	public CreateProblemDTOBuilder withDescription(String description) {
		this.problemDto.setDescription(description);
		return this;
	}
	
	public CreateProblemDTO build() {
		return this.problemDto;
	}	
}

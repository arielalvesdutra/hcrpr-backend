package dev.arielalvesdutra.hcrpr.builders.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateProblemDTO;

public class UpdateProblemDTOBuilder {
	private UpdateProblemDTO problemDto = new UpdateProblemDTO();
	
	public UpdateProblemDTOBuilder withName(String name) {
		this.problemDto.setName(name);
		return this;
	}
	
	public UpdateProblemDTOBuilder withDescription(String description) {
		this.problemDto.setDescription(description);
		return this;
	}
	
	public UpdateProblemDTO build() {
		return this.problemDto;
	}	
}

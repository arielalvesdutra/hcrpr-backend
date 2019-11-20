package dev.arielalvesdutra.hcrpr.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.CreateSolutionAttemptDTO;

public class CreateSolutionAttemptDTOBuilder {
	private CreateSolutionAttemptDTO solutionAttemptDto = new CreateSolutionAttemptDTO();
	
	public CreateSolutionAttemptDTOBuilder withName(String name) {
		this.solutionAttemptDto.setName(name);
		return this;
	}
	
	public CreateSolutionAttemptDTOBuilder withDescription(String description) {
		this.solutionAttemptDto.setDescription(description);
		return this;
	}
	
	public CreateSolutionAttemptDTO build() {
		return this.solutionAttemptDto;
	}	
}

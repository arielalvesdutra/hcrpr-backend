package dev.arielalvesdutra.hcrpr.builders.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateSolutionAttemptDTO;

public class UpdateSolutionAttemptDTOBuilder {
	private UpdateSolutionAttemptDTO solutionAttemptDto = new UpdateSolutionAttemptDTO();
	
	public UpdateSolutionAttemptDTOBuilder withName(String name) {
		this.solutionAttemptDto.setName(name);
		return this;
	}
	
	public UpdateSolutionAttemptDTOBuilder withDescription(String description) {
		this.solutionAttemptDto.setDescription(description);
		return this;
	}
	
	public UpdateSolutionAttemptDTO build() {
		return this.solutionAttemptDto;
	}	
}

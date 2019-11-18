package dev.arielalvesdutra.hcrpr.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateConceptDTO;

public class UpdateConceptDTOBuilder {
	private UpdateConceptDTO conceptDto = new UpdateConceptDTO();
	
	public UpdateConceptDTOBuilder withName(String name) {
		this.conceptDto.setName(name);
		return this;
	}
	
	public UpdateConceptDTOBuilder withDescription(String description) {
		this.conceptDto.setDescription(description);
		return this;
	}
	
	public UpdateConceptDTO build() {
		return this.conceptDto;
	}	
}
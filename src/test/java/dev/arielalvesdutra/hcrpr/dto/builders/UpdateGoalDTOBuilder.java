package dev.arielalvesdutra.hcrpr.dto.builders;

import dev.arielalvesdutra.hcrpr.controllers.dto.UpdateGoalDTO;

public class UpdateGoalDTOBuilder {
	private UpdateGoalDTO updatedGoalDto = new UpdateGoalDTO();
	
	public UpdateGoalDTOBuilder withDescription(String description) {
		this.updatedGoalDto.setDescription(description);
		return this;
	}
	
	public UpdateGoalDTOBuilder withAchieved(boolean achieved) {
		this.updatedGoalDto.setAchieved(achieved);
		return this;
	}
	
	public UpdateGoalDTO build() {
		return this.updatedGoalDto;
	}
}

package dev.arielalvesdutra.hcrpr.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.Goal;

public class CreateGoalDTO {
	
	@NotEmpty
	@Size(min = 5)
	private String description;
	
	public CreateGoalDTO() { }
	
	public CreateGoalDTO(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Goal toGoal() {
		Goal goal = new Goal();
		goal.setDescription(this.getDescription());
		return goal;
	}
}

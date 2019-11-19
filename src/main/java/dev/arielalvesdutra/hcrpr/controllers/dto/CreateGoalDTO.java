package dev.arielalvesdutra.hcrpr.controllers.dto;

import dev.arielalvesdutra.hcrpr.entities.Goal;

public class CreateGoalDTO {
	
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

package dev.arielalvesdutra.hcrpr.controllers.dto;

import dev.arielalvesdutra.hcrpr.entities.Goal;

public class UpdateGoalDTO {
	
	private String description;
	
	private boolean achieved;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAchieved() {
		return achieved;
	}

	public void setAchieved(boolean achieved) {
		this.achieved = achieved;
	}

	public Goal toGoal() {
		Goal goal = new Goal();
		goal.setAchieved(this.isAchieved());
		goal.setDescription(this.getDescription());
		return goal;
	}
}

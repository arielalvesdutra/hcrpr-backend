package dev.arielalvesdutra.hcrpr.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;

public class UpdateSolutionAttemptDTO {

	@NotEmpty
	@Size(min = 5)
	private String name;
	
	@NotEmpty
	@Size(min = 5)
	private String description;
	
	public UpdateSolutionAttemptDTO() { }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SolutionAttempt toSolutionAttempt() {
		SolutionAttempt solutionAttempt = new SolutionAttempt();
		solutionAttempt.setName(this.getName());
		solutionAttempt.setDescription(this.getDescription());
		
		return solutionAttempt;
	}

	@Override
	public String toString() {
		return "UpdateSolutionAttemptDTO [name=" + name + ", description=" + description + "]";
	}
}

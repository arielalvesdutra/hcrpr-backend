package dev.arielalvesdutra.hcrpr.controllers.dto;

import dev.arielalvesdutra.hcrpr.entities.Problem;

public class CreateProblemDTO {

	private String name;
	
	private String description;
	
	public CreateProblemDTO() { }

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

	public Problem toProblem() {
		Problem problem = new Problem();
		problem.setName(this.getName());
		problem.setDescription(this.getDescription());
		
		return problem;
	}

	@Override
	public String toString() {
		return "CreateProblemDTO [name=" + name + ", description=" + description + "]";
	}
}

package dev.arielalvesdutra.hcrpr.builders;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;

public class SolutionAttemptBuilder {
	
	private SolutionAttempt solutionAttempt = new SolutionAttempt();
	
	public SolutionAttemptBuilder withName(String name) {
		this.solutionAttempt.setName(name);
		return this;
	}
	
	public SolutionAttemptBuilder withDescription(String name) {
		this.solutionAttempt.setName(name);
		return this;
	}
	
	public SolutionAttempt build() {
		return this.solutionAttempt;
	}
}

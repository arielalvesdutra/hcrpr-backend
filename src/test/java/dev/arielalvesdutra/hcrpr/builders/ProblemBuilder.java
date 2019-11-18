package dev.arielalvesdutra.hcrpr.builders;

import java.util.Set;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;

public class ProblemBuilder {
	
	private Problem problem = new Problem();

	public ProblemBuilder withName(String name) {
		this.problem.setName(name);
		return this;
	}
	
	public ProblemBuilder withDescription(String description) {
		this.problem.setDescription(description);
		return this;
	}
	
	public ProblemBuilder withRelatedConcepts(Set<Concept> relatedConcepts) {
		this.problem.setRelatedConcepts(relatedConcepts);
		return this;
	}
	
	public Problem build() {
		return this.problem;
	}
}

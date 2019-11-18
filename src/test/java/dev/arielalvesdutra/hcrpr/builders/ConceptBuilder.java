package dev.arielalvesdutra.hcrpr.builders;

import dev.arielalvesdutra.hcrpr.entities.Concept;

public class ConceptBuilder {
	
	private Concept concept = new Concept();
	
	public ConceptBuilder withName(String name) {
		this.concept.setName(name);
		return this;
	}
	
	public ConceptBuilder withDescription(String name) {
		this.concept.setName(name);
		return this;
	}
	
	public Concept build() {
		return this.concept;
	}
}

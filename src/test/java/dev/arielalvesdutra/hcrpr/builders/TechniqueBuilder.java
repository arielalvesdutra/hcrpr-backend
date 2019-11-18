package dev.arielalvesdutra.hcrpr.builders;

import dev.arielalvesdutra.hcrpr.entities.Technique;

public class TechniqueBuilder {

	private Technique technique = new Technique();
	
	public TechniqueBuilder withName(String name) {
		this.technique.setName(name);
		return this;
	}
	
	public TechniqueBuilder withDescription(String description) {
		this.technique.setDescription(description);
		return this;
	}
	
	public Technique build() {
		return this.technique;
	}
}

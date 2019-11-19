package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateProblemConceptsDTO {
	
	private List<Long> conceptsIds =  new ArrayList<Long>();

	public List<Long> getConceptsIds() {
		return conceptsIds;
	}

	public void setConceptsIds(List<Long> conceptsIds) {
		this.conceptsIds = conceptsIds;
	}
}

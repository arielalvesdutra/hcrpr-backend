package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateSolutionAttemptTechniquesDTO {
	
	private List<Long> techniquesIds =  new ArrayList<Long>();

	public List<Long> getTechniquesIds() {
		return techniquesIds;
	}

	public void setTechniquesIds(List<Long> techniquesIds) {
		this.techniquesIds = techniquesIds;
	}

}

package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;

import dev.arielalvesdutra.hcrpr.entities.Goal;

public class RetrieveGoalDTO {
	private Long id;
	
	private String description;

	private OffsetDateTime createdAt = OffsetDateTime.now();
	
	private Boolean achieved = false;
	
	public RetrieveGoalDTO() {}
	
	public RetrieveGoalDTO(Goal goal) {
		this.setId(goal.getId());
		this.setAchieved(goal.getAchieved());
		this.setDescription(goal.getDescription());
		this.setCreatedAt(goal.getCreatedAt());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getAchieved() {
		return achieved;
	}

	public void setAchieved(Boolean achieved) {
		this.achieved = achieved;
	}

	public static Page<RetrieveGoalDTO> fromGoalPageToRetrieveGoalDTOPage(Page<Goal> goalsPage) {
		return goalsPage.map(RetrieveGoalDTO::new);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achieved == null) ? 0 : achieved.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetrieveGoalDTO other = (RetrieveGoalDTO) obj;
		if (achieved == null) {
			if (other.achieved != null)
				return false;
		} else if (!achieved.equals(other.achieved))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.isEqual(other.createdAt))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RetrieveGoalDTO [id=" + id + ", description=" + description + ", createdAt=" + createdAt + ", achieved="
				+ achieved + "]";
	}
}

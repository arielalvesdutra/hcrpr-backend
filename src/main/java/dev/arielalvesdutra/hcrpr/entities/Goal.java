package dev.arielalvesdutra.hcrpr.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Goal implements Serializable {
	
	private static final long serialVersionUID = 3889101295961466597L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();
	
	private Boolean achieved = false;
	
	@JsonIgnore
	@ManyToOne
	private Problem problem;
	
	public Goal() {	}
	
	public Goal(String description) {
		this.description = description;
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

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Goal))
			return false;
		Goal other = (Goal) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Goal [id=" + id + ", description=" + description + ", createdAt=" + createdAt + ", achieved=" + achieved
				+ "]";
	}
}

package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.entities.Goal;
import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;

public class RetrieveProblemDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private OffsetDateTime createdAt;

	private Set<ProblemComment> comments;
	
	private Set<Concept> relatedConcepts;
	
	private Set<Goal> goals;
	
	private Set<SolutionAttempt> solutionAttempts;

	public RetrieveProblemDTO() { }

	public RetrieveProblemDTO(Problem problem) {
		this.setId(problem.getId());
		this.setName(problem.getName());
		this.setDescription(problem.getDescription());
		this.setCreatedAt(problem.getCreatedAt());
		this.setComments(problem.getComments());
		this.setRelatedConcepts(problem.getRelatedConcepts());
		this.setGoals(problem.getGoals());
		this.setSolutionAttempts(problem.getSolutionAttempts());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public static Page<RetrieveProblemDTO> fromProblemPageToRetrieveProblemDTOPage(
			Page<Problem> problemsPage) {
			
		return problemsPage.map(RetrieveProblemDTO::new);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof RetrieveProblemDTO))
			return false;
		RetrieveProblemDTO other = (RetrieveProblemDTO) obj;
		return createdAt.isEqual(other.createdAt) && 
				Objects.equals(description, other.description) && 
				Objects.equals(id, other.id) && 
				Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RetrieveProblemDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdAt="
				+ createdAt + "]";
	}
	
	public Set<ProblemComment> getComments() {
		return comments;
	}

	public void setComments(Set<ProblemComment> comments) {
		this.comments = comments;
	}
	
	
	public Set<Concept> getRelatedConcepts() {
		return relatedConcepts;
	}

	public void setRelatedConcepts(Set<Concept> relatedConcepts) {
		this.relatedConcepts = relatedConcepts;
	}

	public Set<Goal> getGoals() {
		return goals;
	}

	public void setGoals(Set<Goal> goals) {
		this.goals = goals;
	}

	public Set<SolutionAttempt> getSolutionAttempts() {
		return solutionAttempts;
	}

	public void setSolutionAttempts(Set<SolutionAttempt> solutionAttempts) {
		this.solutionAttempts = solutionAttempts;
	}
}

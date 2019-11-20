package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;

import dev.arielalvesdutra.hcrpr.entities.Problem;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;
import dev.arielalvesdutra.hcrpr.entities.Technique;

public class RetrieveSolutionAttemptDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String learned;
	
	private String tencendy;
	
	private OffsetDateTime createdAt;
	
	private Problem problem;
	
	private Set<SolutionAttemptComment> comments;
	
	private Set<Technique> techniques;
	
	public RetrieveSolutionAttemptDTO() { }

	public RetrieveSolutionAttemptDTO(SolutionAttempt solutionAttempt) {
		this.setId(solutionAttempt.getId());
		this.setName(solutionAttempt.getName());
		this.setDescription(solutionAttempt.getDescription());
		this.setCreatedAt(solutionAttempt.getCreatedAt());
		this.setComments(solutionAttempt.getComments());
		this.setTechniques(solutionAttempt.getTechniques());
		this.setProblem(solutionAttempt.getProblem());
		this.setTencendy(solutionAttempt.getTendency());
		this.setLearned(solutionAttempt.getLearned());
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

	public Set<SolutionAttemptComment> getComments() {
		return comments;
	}

	public void setComments(Set<SolutionAttemptComment> comments) {
		this.comments = comments;
	}

	public Set<Technique> getTechniques() {
		return techniques;
	}

	public void setTechniques(Set<Technique> techniques) {
		this.techniques = techniques;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public String getLearned() {
		return learned;
	}

	public void setLearned(String learned) {
		this.learned = learned;
	}

	public String getTencendy() {
		return tencendy;
	}

	public void setTencendy(String tencendy) {
		this.tencendy = tencendy;
	}

	public static Page<RetrieveSolutionAttemptDTO> fromSolutionAttemptPageToRetrieveSolutionAttemptDTOPage(
			Page<SolutionAttempt> solutionAttemptsPage) {
			
		return solutionAttemptsPage.map(RetrieveSolutionAttemptDTO::new);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof RetrieveSolutionAttemptDTO))
			return false;
		RetrieveSolutionAttemptDTO other = (RetrieveSolutionAttemptDTO) obj;
		return createdAt.isEqual(other.createdAt) && 
				Objects.equals(description, other.description) && 
				Objects.equals(id, other.id) && 
				Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RetrieveSolutionAttemptDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdAt="
				+ createdAt + "]";
	}
	
	
}

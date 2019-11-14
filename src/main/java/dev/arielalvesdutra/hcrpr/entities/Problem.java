package dev.arielalvesdutra.hcrpr.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Problem implements Serializable {
	
	private static final long serialVersionUID = -5653298291650378229L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String description;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();
	
	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
	private Set<ProblemComment> comments = new HashSet<ProblemComment>();
	
	@ManyToMany
	private Set<Concept> relatedConcepts = new HashSet<Concept>();
	
	@OneToMany
	private Set<Goal> goals;
	
	@OneToMany
	private Set<SolutionAttempt> solutionAttempts = new HashSet<SolutionAttempt>();
	
	@ManyToMany
	private Set<Problem> relatedProblems = new HashSet<Problem>();

	public Problem() { }
	
	public Problem(String name) {
		this.name = name;
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

	public void setComments(Set<ProblemComment> comments) {
		this.comments = comments;		
	}
	
	public Set<ProblemComment> getComments() {
		return comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Problem other = (Problem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", name=" + name + ", comments=" + comments + "]";
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

	public Set<Concept> getRelatedConcepts() {
		return relatedConcepts;
	}

	public void setRelatedConcepts(Set<Concept> concepts) {
		this.relatedConcepts = concepts;
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

	public Set<Problem> getRelatedProblems() {
		return relatedProblems;
	}

	public void setRelatedProblems(Set<Problem> relatedProblems) {
		this.relatedProblems = relatedProblems;
	}
}

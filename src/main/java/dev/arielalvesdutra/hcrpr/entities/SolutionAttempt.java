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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SolutionAttempt implements Serializable {
	
	private static final long serialVersionUID = -4801555031990433754L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private String tendency;
	
	private String learned;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();

	@OneToMany(mappedBy = "solutionAttempt", cascade = CascadeType.ALL)
	private Set<SolutionAttemptComment> comments = new HashSet<SolutionAttemptComment>();
	
	@ManyToOne
	private Problem problem;
	
	@ManyToMany
	@JoinTable(name= "solution_attempt_technique",
		inverseJoinColumns = @JoinColumn(name = "solution_attempt_id", referencedColumnName = "id"),
		joinColumns = @JoinColumn(name = "technique_id", referencedColumnName = "id"))
	private Set<Technique> techniques = new HashSet<Technique>();

	public SolutionAttempt() { }
	
	public SolutionAttempt(String name) {
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

	public void setComments(Set<SolutionAttemptComment> comments) {
		this.comments  = comments;		
	}
	
	public Set<SolutionAttemptComment> getComments() {
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
		SolutionAttempt other = (SolutionAttempt) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SolutionAttempt [id=" + id + ", name=" + name + ", comments=" + comments + "]";
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
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

	public Set<Technique> getTechniques() {
		return techniques;
	}

	public void setTechniques(Set<Technique> techniques) {
		this.techniques = techniques;
	}

	public String getTendency() {
		return tendency;
	}

	public void setTendency(String tendancy) {
		this.tendency = tendancy;
	}

	public String getLearned() {
		return learned;
	}

	public void setLearned(String learned) {
		this.learned = learned;
	}
}

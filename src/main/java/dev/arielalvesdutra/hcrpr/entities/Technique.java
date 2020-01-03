package dev.arielalvesdutra.hcrpr.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Technique implements Serializable {
	
	private static final long serialVersionUID = -4150441466770076521L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();	
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name= "solution_attempt_technique",
		joinColumns = @JoinColumn(name = "technique_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "solution_attempt_id", referencedColumnName = "id"))
	private Set<SolutionAttempt> solutionAttempts = new HashSet<SolutionAttempt>();
	
	public Technique() {}
	
	public Technique(String name) {
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

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<SolutionAttempt> getSolutionAttempts() {
		return solutionAttempts;
	}

	public void setSolutionAttempts(Set<SolutionAttempt> solutionAttempts) {
		this.solutionAttempts = solutionAttempts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Technique))
			return false;
		Technique other = (Technique) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Technique [id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt
				+ "]";
	}
}


package dev.arielalvesdutra.hcrpr.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Concept implements Serializable {
	
	private static final long serialVersionUID = 1923023117003264692L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private OffsetDateTime createdAt;
	
	@ManyToMany
	@JoinTable(name= "problem_concept",
		inverseJoinColumns = @JoinColumn(name = "concept_id", referencedColumnName = "id"),
		joinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "id"))
	private Set<Problem> problems = new HashSet<Problem>();

	public Concept() { }
	
	public Concept(String name) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Concept))
			return false;
		Concept other = (Concept) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Concept [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Set<Problem> getProblems() {
		return problems;
	}

	public void setProblems(Set<Problem> problems) {
		this.problems = problems;
	}
}

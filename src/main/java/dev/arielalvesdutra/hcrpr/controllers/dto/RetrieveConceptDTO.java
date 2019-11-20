package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;

import dev.arielalvesdutra.hcrpr.entities.Concept;

public class RetrieveConceptDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private OffsetDateTime createdAt;
	
	public RetrieveConceptDTO() { }

	public RetrieveConceptDTO(Concept createdConcept) {
		this.setId(createdConcept.getId());
		this.setName(createdConcept.getName());
		this.setDescription(createdConcept.getDescription());
		this.setCreatedAt(createdConcept.getCreatedAt());
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

	public static Page<RetrieveConceptDTO> fromConceptPageToRetrieveConceptDTOPage(
			Page<Concept> conceptsPage) {
			
		return conceptsPage.map(RetrieveConceptDTO::new);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof RetrieveConceptDTO))
			return false;
		RetrieveConceptDTO other = (RetrieveConceptDTO) obj;
		return createdAt.isEqual(other.createdAt) && 
				Objects.equals(description, other.description) && 
				Objects.equals(id, other.id) && 
				Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RetrieveConceptDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdAt="
				+ createdAt + "]";
	}

	public static List<RetrieveConceptDTO> fromConceptSetToRetrieveConceptDTOList(
			Set<Concept> concepts) {
		List<RetrieveConceptDTO> retrieveConceptsDto = new ArrayList<RetrieveConceptDTO>();
		
		for (Concept concept : concepts) {
			retrieveConceptsDto.add(new RetrieveConceptDTO(concept));
		}
		
		return retrieveConceptsDto;
	}
	
	
}

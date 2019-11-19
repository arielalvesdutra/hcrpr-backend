package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import dev.arielalvesdutra.hcrpr.entities.Technique;

public class RetrieveTechniqueDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private OffsetDateTime createdAt;
	
	public RetrieveTechniqueDTO() { }

	public RetrieveTechniqueDTO(Technique createdTechnique) {
		this.setId(createdTechnique.getId());
		this.setName(createdTechnique.getName());
		this.setDescription(createdTechnique.getDescription());
		this.setCreatedAt(createdTechnique.getCreatedAt());
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

	public static Page<RetrieveTechniqueDTO> fromTechniquePageToRetrieveTechniqueDTOPage(
			Page<Technique> techniquesPage) {
			
		return techniquesPage.map(RetrieveTechniqueDTO::new);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof RetrieveTechniqueDTO))
			return false;
		RetrieveTechniqueDTO other = (RetrieveTechniqueDTO) obj;
		return createdAt.isEqual(other.createdAt) && 
				Objects.equals(description, other.description) && 
				Objects.equals(id, other.id) && 
				Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RetrieveTechniqueDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdAt="
				+ createdAt + "]";
	}

	public static Page<RetrieveTechniqueDTO> fromTechniqueSetToRetrieveTechniqueDTOPage(
			Set<Technique> techniquesSet) {
		
		List<Technique> techniquesList = new ArrayList<>(techniquesSet);
		PageImpl<Technique> techniquesPage = new PageImpl<Technique>(techniquesList);
		
		return RetrieveTechniqueDTO.fromTechniquePageToRetrieveTechniqueDTOPage(techniquesPage);
	}
	
	
}

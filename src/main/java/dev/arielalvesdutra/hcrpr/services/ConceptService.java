package dev.arielalvesdutra.hcrpr.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.arielalvesdutra.hcrpr.entities.Concept;
import dev.arielalvesdutra.hcrpr.repositories.ConceptRepository;

@Service
public class ConceptService {

	@Autowired
	private ConceptRepository conceptRepository;
	
	public Concept create(Concept concept) {
		
		Concept createdConcept = this.conceptRepository.save(concept);
		
		return createdConcept;
	}

	public List<Concept> findAll() {
		return this.conceptRepository.findAll();
	}

	public Page<Concept> findAll(Pageable pageable) {
		return this.conceptRepository.findAll(pageable);
	}

	public Concept findById(Long id) {
		return this.conceptRepository
				.findById(id)
				.orElseThrow(() -> 
				new EntityNotFoundException("Conceito com ID "+ id +" não encontrado"));
	}

	public void deleteById(Long id) {
		Concept concept = this.findById(id);
		
		this.conceptRepository.delete(concept);		
	}

	public Concept update(Long id, Concept parameterConcept) {
		Concept existintConcept = this.findById(id);
		
		existintConcept.setName(parameterConcept.getName());
		existintConcept.setDescription(parameterConcept.getDescription());
		this.conceptRepository.save(existintConcept);
		
		return existintConcept;		
	}

	public List<Concept> findByIds(List<Long> ids) {
		return this.conceptRepository.findAllById(ids);
	}
}

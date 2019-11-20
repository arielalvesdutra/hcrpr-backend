package dev.arielalvesdutra.hcrpr.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.arielalvesdutra.hcrpr.entities.Technique;
import dev.arielalvesdutra.hcrpr.repositories.TechniqueRepository;

@Service
public class TechniqueService {

	@Autowired
	private TechniqueRepository techniqueRepository;

	public Technique create(Technique technique) {
		return this.techniqueRepository.save(technique);
	}
	
	public void deleteById(Long id) {
		Technique technique = this.findById(id);
		
		this.techniqueRepository.delete(technique);
	}

	public List<Technique> findAll() {
		return this.techniqueRepository.findAll();
	}
	
	public Page<Technique> findAll(Pageable pageable) {
		return this.techniqueRepository.findAll(pageable);
	}

	public Technique findById(Long id) {
		return this.techniqueRepository
				.findById(id).
				orElseThrow(() -> 
				new EntityNotFoundException("Técnica com ID "+ id +" não encontrada"));
	}

	public Technique update(Long id, Technique parameterTechnique) {
		Technique existingTechnique = this.findById(id);
		
		existingTechnique.setName(parameterTechnique.getName());
		existingTechnique.setDescription(parameterTechnique.getDescription());
		
		this.techniqueRepository.save(existingTechnique);
		
		return existingTechnique;
	}

	public List<Technique> findByIds(List<Long> ids) {
		return this.techniqueRepository.findAllById(ids);
	}
}

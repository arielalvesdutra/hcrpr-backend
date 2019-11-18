package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.Technique;

public interface TechniqueRepository extends JpaRepository<Technique, Long>{

}

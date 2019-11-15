package dev.arielalvesdutra.hcrpr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arielalvesdutra.hcrpr.entities.Concept;


public interface ConceptRepository extends JpaRepository<Concept, Long> {

}

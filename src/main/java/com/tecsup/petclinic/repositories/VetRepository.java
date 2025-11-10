package com.tecsup.petclinic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecsup.petclinic.entities.Vet;

/**
 * Repositorio para la entidad Vet.
 */
@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {
    
}
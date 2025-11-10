package com.tecsup.petclinic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecsup.petclinic.entities.Vet;

/**
 * Repositorio para la entidad Vet.
 */
@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {
    
    // Método de ejemplo para búsqueda por nombre, si se necesitara
    // List<Vet> findByLastName(String lastName);
}
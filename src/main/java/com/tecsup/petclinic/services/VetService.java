package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

/**
 * Interfaz para la lógica de negocio de la entidad Vet.
 */
public interface VetService {
    
    Vet create(Vet vet);
    
    Vet findById(Integer id) throws VetNotFoundException;
    
    Vet update(Vet vet) throws VetNotFoundException;
    
    void delete(Integer id) throws VetNotFoundException;
    
    List<Vet> findAll();
}
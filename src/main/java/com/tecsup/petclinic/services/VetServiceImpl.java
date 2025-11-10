package com.tecsup.petclinic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;

/**
 * Implementación de la lógica de negocio para la entidad Vet.
 */
@Service
public class VetServiceImpl implements VetService {

    @Autowired
    private VetRepository vetRepository;

    @Override
    public Vet create(Vet vet) {
        return vetRepository.save(vet);
    }

    @Override
    public Vet findById(Integer id) throws VetNotFoundException {
        Optional<Vet> vet = vetRepository.findById(id);
        if (!vet.isPresent())
            throw new VetNotFoundException("Vet not found with ID: " + id);
        return vet.get();
    }

    @Override
    public Vet update(Vet newVet) throws VetNotFoundException {
        Vet currentVet = findById(newVet.getId());
        
        // Actualiza solo los campos principales para simplificar
        currentVet.setFirstName(newVet.getFirstName());
        currentVet.setLastName(newVet.getLastName());
        
        // Si la entidad Vet tuviera más campos (email, phone, active) se actualizarían aquí.
        
        return vetRepository.save(currentVet);
    }

    @Override
    public void delete(Integer id) throws VetNotFoundException {
        Vet vet = findById(id);
        vetRepository.delete(vet);
    }

    @Override
    public List<Vet> findAll() {
        return vetRepository.findAll();
    }
}
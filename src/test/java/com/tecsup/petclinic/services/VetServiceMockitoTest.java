package com.tecsup.petclinic.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.util.TObjectCreator;

/**
 * Pruebas unitarias para VetServiceImpl usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class VetServiceMockitoTest {

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private VetServiceImpl vetService;

    // --- FIND ---

    @Test
    void testFindVetByIdOK() throws VetNotFoundException {
        Vet mockVet = TObjectCreator.getVet();
        
        // Simular que el repositorio encuentra la mascota
        Mockito.when(vetRepository.findById(mockVet.getId()))
                .thenReturn(Optional.of(mockVet));

        Vet foundVet = vetService.findById(mockVet.getId());
        
        assertNotNull(foundVet);
        assertEquals(mockVet.getId(), foundVet.getId());

        // Verificar que el método del repositorio fue llamado
        Mockito.verify(vetRepository, Mockito.times(1)).findById(mockVet.getId());
    }

    @Test
    void testFindVetByIdKO() {
        final int ID_NOT_EXIST = 999;

        // Simular que el repositorio no encuentra la mascota
        Mockito.when(vetRepository.findById(ID_NOT_EXIST))
                .thenReturn(Optional.empty());

        assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(ID_NOT_EXIST);
        });
    }

    @Test
    void testFindAllVets() {
        List<Vet> mockVets = TObjectCreator.getAllVets();
        
        Mockito.when(vetRepository.findAll())
                .thenReturn(mockVets);
        
        List<Vet> foundVets = vetService.findAll();
        
        assertFalse(foundVets.isEmpty());
        assertEquals(mockVets.size(), foundVets.size());
    }
    
    // --- CREATE ---
    
    @Test
    void testCreateVet() {
        Vet newVet = TObjectCreator.newVet();
        Vet createdVet = TObjectCreator.newVetCreated();

        Mockito.when(vetRepository.save(newVet))
                .thenReturn(createdVet);
        
        Vet result = vetService.create(newVet);
        
        assertNotNull(result.getId());
        assertEquals(createdVet.getFirstName(), result.getFirstName());
    }

    // --- DELETE ---

    @Test
    void testDeleteVetOK() throws VetNotFoundException {
        Vet vetToDelete = TObjectCreator.newVetCreated();
        
        // Simular el findById exitoso
        Mockito.when(vetRepository.findById(vetToDelete.getId()))
                .thenReturn(Optional.of(vetToDelete));
        
        // Simular la eliminación (no devuelve nada)
        Mockito.doNothing().when(vetRepository).delete(vetToDelete);

        vetService.delete(vetToDelete.getId());
        
        // Verificar que findById y delete fueron llamados
        Mockito.verify(vetRepository, Mockito.times(1)).findById(vetToDelete.getId());
        Mockito.verify(vetRepository, Mockito.times(1)).delete(vetToDelete);
    }
}
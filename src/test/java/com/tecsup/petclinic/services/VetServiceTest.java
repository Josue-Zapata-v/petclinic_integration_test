package com.tecsup.petclinic.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.util.TObjectCreator;

import lombok.extern.slf4j.Slf4j;

/**
 * Pruebas de integración para la capa de servicio VetService.
 */
@SpringBootTest
@Slf4j
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    // --- CREATE ---

    @Test
    @Transactional // Para asegurar la limpieza de la DB después del test
    void testCreateVet() {
        Vet newVet = TObjectCreator.newVet();
        Vet createdVet = vetService.create(newVet);

        assertNotNull(createdVet.getId());
        assertEquals("Antonio", createdVet.getFirstName());
        assertEquals("Banderas", createdVet.getLastName());

        log.info("Vet creado: {}", createdVet);
    }

    // --- FIND ---

    @Test
    void testFindVetByIdOK() throws VetNotFoundException {
        // ID 1 existe en data.sql
        final int VET_ID = 1;
        Vet vet = vetService.findById(VET_ID);
        
        assertNotNull(vet);
        assertEquals(VET_ID, vet.getId());
        assertEquals("James", vet.getFirstName());
        
        log.info("Vet encontrado: {}", vet);
    }

    @Test
    void testFindVetByIdKO() {
        final int ID_NOT_EXIST = 999;
        
        Assertions.assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(ID_NOT_EXIST);
        });
    }
    
    @Test
    void testFindAllVets() {
        List<Vet> vets = vetService.findAll();
        // Basado en data.sql, hay 6 veterinarios
        assertEquals(6, vets.size());
    }

    // --- UPDATE ---

    @Test
    @Transactional
    void testUpdateVetOK() throws VetNotFoundException {
        // 1. Crear un Vet
        Vet newVet = TObjectCreator.newVet();
        Vet createdVet = vetService.create(newVet);
        
        // 2. Actualizar datos
        final String UP_FIRST_NAME = "Antonio-Updated";
        final String UP_LAST_NAME = "Banderas-Updated";

        createdVet.setFirstName(UP_FIRST_NAME);
        createdVet.setLastName(UP_LAST_NAME);
        
        // 3. Persistir actualización
        Vet updatedVet = vetService.update(createdVet);

        // 4. Verificar
        assertEquals(createdVet.getId(), updatedVet.getId());
        assertEquals(UP_FIRST_NAME, updatedVet.getFirstName());
        assertEquals(UP_LAST_NAME, updatedVet.getLastName());

        log.info("Vet actualizado: {}", updatedVet);
    }

    // --- DELETE ---

    @Test
    @Transactional
    void testDeleteVetOK() throws VetNotFoundException {
        // 1. Crear Vet
        Vet newVet = TObjectCreator.newVet();
        Vet createdVet = vetService.create(newVet);
        Integer createdId = createdVet.getId();

        log.info("Vet a eliminar: {}", createdVet);

        // 2. Eliminar
        vetService.delete(createdId);

        // 3. Verificar que ya no existe
        Assertions.assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(createdId);
        });
    }

    @Test
    void testDeleteVetKO() {
        final int ID_NOT_EXIST = 888;
        
        Assertions.assertThrows(VetNotFoundException.class, () -> {
            vetService.delete(ID_NOT_EXIST);
        });
    }
}
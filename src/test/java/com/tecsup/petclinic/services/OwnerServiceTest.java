package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.util.TObjectCreatorOwner;

import lombok.extern.slf4j.Slf4j;

/**
 * Pruebas de integración para la capa de servicio OwnerService.
 * Utiliza @SpringBootTest para cargar el contexto completo y la base de datos H2.
 */
@SpringBootTest
@Slf4j
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    // --- CREATE ---

    @Test
    @Transactional
    void testCreateOwner() {
        Owner newOwner = TObjectCreatorOwner.newOwner();
        Owner createdOwner = ownerService.create(newOwner);

        // Verificaciones
        assertNotNull(createdOwner.getId());
        assertEquals("Antonio", createdOwner.getFirstName());
        assertEquals("Banderas", createdOwner.getLastName());

        log.info("Owner creado: {}", createdOwner);
    }

    // --- FIND ---

    @Test
    void testFindOwnerByIdOK() throws OwnerNotFoundException {
        // ID 1 existe en data.sql (George Franklin)
        final long OWNER_ID = 1L;
        Owner owner = ownerService.findById(OWNER_ID);

        assertNotNull(owner);
        assertEquals(OWNER_ID, owner.getId());
        assertEquals("George", owner.getFirstName());

        log.info("Owner encontrado: {}", owner);
    }

    @Test
    void testFindOwnerByIdKO() {
        final long ID_NOT_EXIST = 999L;

        // Debe lanzar una excepción si el ID no existe
        Assertions.assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(ID_NOT_EXIST);
        });
    }

    @Test
    void testFindAllOwners() {
        List<Owner> owners = ownerService.findAll();
        // Basado en data.sql, hay 10 dueños
        assertEquals(10, owners.size());
    }

    // --- UPDATE ---

    @Test
    @Transactional
    void testUpdateOwnerOK() throws OwnerNotFoundException {
        // 1. Crear un Owner (para garantizar un registro fresco)
        Owner newOwner = TObjectCreatorOwner.newOwner();
        Owner createdOwner = ownerService.create(newOwner);

        // 2. Actualizar datos
        final String UP_FIRST_NAME = "Antonio-Updated";
        final String UP_TELEPHONE = "987654321";

        createdOwner.setFirstName(UP_FIRST_NAME);
        createdOwner.setTelephone(UP_TELEPHONE);

        // 3. Persistir actualización
        Owner updatedOwner = ownerService.update(createdOwner);

        // 4. Verificar
        assertEquals(createdOwner.getId(), updatedOwner.getId());
        assertEquals(UP_FIRST_NAME, updatedOwner.getFirstName());
        assertEquals(UP_TELEPHONE, updatedOwner.getTelephone());

        log.info("Owner actualizado: {}", updatedOwner);
    }

    // NOTA: No se necesita un testUpdateOwnerKO ya que el método update() guarda si existe o lo crea si no existe (upsert behavior)
    // En esta implementación, update usa .save(), que es un upsert. Si se quisiera verificar la actualización de un registro existente,
    // se usaría findById previamente como hicimos en testUpdateOwnerOK.

    // --- DELETE ---

    @Test
    @Transactional
    void testDeleteOwnerOK() throws OwnerNotFoundException {
        // 1. Crear Owner
        Owner newOwner = TObjectCreatorOwner.newOwner();
        Owner createdOwner = ownerService.create(newOwner);
        Long createdId = createdOwner.getId();

        log.info("Owner a eliminar: {}", createdOwner);

        // 2. Eliminar
        ownerService.delete(createdId);

        // 3. Verificar que ya no existe
        Assertions.assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(createdId);
        });
    }

    @Test
    void testDeleteOwnerKO() {
        final long ID_NOT_EXIST = 888L;

        // Debe lanzar una excepción si intentamos eliminar un registro inexistente
        Assertions.assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.delete(ID_NOT_EXIST);
        });
    }
}
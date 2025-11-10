package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;
import com.tecsup.petclinic.util.TObjectCreatorOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * Pruebas unitarias para OwnerService usando Mockito para simular OwnerRepository.
 * Solo se prueba la lógica de la capa de servicio, no la interacción con la base de datos.
 */
@ExtendWith(MockitoExtension.class)
public class OwnerServiceMockitoTest {

    // Inyecta la implementación del servicio, inyectando los mocks en sus dependencias.
    @InjectMocks
    private OwnerServiceImpl ownerService;

    // Simula el repositorio (la capa de datos)
    @Mock
    private OwnerRepository ownerRepository;

    private Owner mockOwner;

    @BeforeEach
    void setUp() {
        // Objeto base para simular respuestas
        mockOwner = TObjectCreatorOwner.getOwner();
    }

    // --- LECTURA ---

    @Test
    void testFindByIdOK() throws OwnerNotFoundException {
        // Configurar el mock: cuando se llame a findById, devuelve el Owner simulado.
        Mockito.when(ownerRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockOwner));

        Owner foundOwner = ownerService.findById(mockOwner.getId());

        assertNotNull(foundOwner);
        assertEquals(mockOwner.getFirstName(), foundOwner.getFirstName());

        // Verificar que el método del repositorio fue llamado exactamente una vez
        Mockito.verify(ownerRepository, Mockito.times(1)).findById(mockOwner.getId());
    }

    @Test
    void testFindByIdKO() {
        // Configurar el mock: cuando se llame a findById, devuelve Optional.empty().
        Mockito.when(ownerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Esperar que el servicio lance OwnerNotFoundException
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(999L);
        });
    }

    @Test
    void testFindAll() {
        List<Owner> mockOwners = TObjectCreatorOwner.getAllOwners();

        // Configurar el mock: devuelve una lista simulada de Owners
        Mockito.when(ownerRepository.findAll())
                .thenReturn(mockOwners);

        List<Owner> foundOwners = ownerService.findAll();

        assertNotNull(foundOwners);
        assertEquals(mockOwners.size(), foundOwners.size());

        // Verificar llamada
        Mockito.verify(ownerRepository, Mockito.times(1)).findAll();
    }

    // --- CREACIÓN ---

    @Test
    void testCreateOwner() {
        Owner newOwner = TObjectCreatorOwner.newOwner();
        Owner createdOwner = TObjectCreatorOwner.newOwnerCreated(); // Objeto con ID simulado

        // Configurar el mock: cuando se llama a save, devuelve la versión "creada" (con ID)
        Mockito.when(ownerRepository.save(any(Owner.class)))
                .thenReturn(createdOwner);

        Owner result = ownerService.create(newOwner);

        assertNotNull(result.getId());
        assertEquals(createdOwner.getFirstName(), result.getFirstName());

        Mockito.verify(ownerRepository, Mockito.times(1)).save(newOwner);
    }

    // --- ACTUALIZACIÓN ---

    @Test
    void testUpdateOwner() {
        Owner originalOwner = TObjectCreatorOwner.getOwner();

        // El método update() usa .save(), simulamos que devuelve los datos actualizados.
        Owner updatedData = TObjectCreatorOwner.getOwner();
        updatedData.setFirstName("UPDATED");

        Mockito.when(ownerRepository.save(any(Owner.class)))
                .thenReturn(updatedData);

        Owner result = ownerService.update(updatedData);

        assertEquals("UPDATED", result.getFirstName());

        Mockito.verify(ownerRepository, Mockito.times(1)).save(updatedData);
    }

    // --- ELIMINACIÓN ---

    @Test
    void testDeleteOwnerOK() throws OwnerNotFoundException {
        final long ID_TO_DELETE = 1L;

        // Necesario para que el servicio encuentre la entidad antes de llamar a delete.
        // Simulamos que el Owner existe
        Mockito.when(ownerRepository.findById(ID_TO_DELETE))
                .thenReturn(Optional.of(mockOwner));

        // Configurar el mock: no hace nada (void method)
        Mockito.doNothing().when(ownerRepository).delete(mockOwner);

        // La ejecución no debe lanzar excepción
        assertDoesNotThrow(() -> ownerService.delete(ID_TO_DELETE));

        // Verificar llamadas: findById y delete deben ser llamados
        Mockito.verify(ownerRepository, Mockito.times(1)).findById(ID_TO_DELETE);
        Mockito.verify(ownerRepository, Mockito.times(1)).delete(mockOwner);
    }

    @Test
    void testDeleteOwnerKO() {
        final long ID_NOT_EXIST = 999L;

        // Simular que el Owner NO existe para findById()
        Mockito.when(ownerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Esperar la excepción OwnerNotFoundException
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.delete(ID_NOT_EXIST);
        });

        // Verificar que el método delete NO fue llamado
        Mockito.verify(ownerRepository, Mockito.times(0)).delete(any(Owner.class));
    }
}
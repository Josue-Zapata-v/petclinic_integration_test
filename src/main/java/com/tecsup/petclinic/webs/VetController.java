package com.tecsup.petclinic.webs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.services.VetService;

/**
 * Controlador REST para manejar las operaciones de la entidad Vet.
 */
@RestController
@RequestMapping("/vets")
public class VetController {

    @Autowired
    private VetService vetService;
    
    @Autowired
    private VetMapper mapper; 

    /**
     * Busca todos los veterinarios.
     * @return Lista de VetDTO.
     */
    @GetMapping
    public ResponseEntity<List<VetDTO>> findAll() {
        List<Vet> vets = vetService.findAll();
        List<VetDTO> vetDTOs = mapper.mapToDtoList(vets);
        return new ResponseEntity<>(vetDTOs, HttpStatus.OK);
    }

    /**
     * Busca un veterinario por su ID.
     * @param id El ID del veterinario.
     * @return VetDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
        try {
            Vet vet = vetService.findById(id);
            VetDTO vetDTO = mapper.mapToDto(vet);
            return new ResponseEntity<>(vetDTO, HttpStatus.OK);
        } catch (VetNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea un nuevo veterinario.
     * @param vetDTO Datos del nuevo veterinario.
     * @return VetDTO del veterinario creado.
     */
    @PostMapping
    public ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
        Vet newVet = vetService.create(mapper.mapToEntity(vetDTO));
        VetDTO newVetDTO = mapper.mapToDto(newVet);
        return new ResponseEntity<>(newVetDTO, HttpStatus.CREATED);
    }

    /**
     * Actualiza un veterinario existente.
     * @param id ID del veterinario a actualizar.
     * @param vetDTO Datos de actualización.
     * @return VetDTO actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VetDTO> update(@PathVariable Integer id, @RequestBody VetDTO vetDTO) {
        VetDTO updatedVetDTO;
        try {
             // Es importante setear el ID al DTO/Entity antes de actualizar
            vetDTO.setId(id);
            Vet updatedVet = vetService.update(mapper.mapToEntity(vetDTO));
            updatedVetDTO = mapper.mapToDto(updatedVet);
            return new ResponseEntity<>(updatedVetDTO, HttpStatus.OK);
        } catch (VetNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un veterinario por su ID.
     * @param id ID del veterinario a eliminar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (VetNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
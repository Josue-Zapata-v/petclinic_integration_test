package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerMapper mapper;

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<OwnerDTO>> findAll() {
        List<Owner> owners = ownerService.findAll();
        return new ResponseEntity<>(mapper.mapToDtoList(owners), HttpStatus.OK);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> findById(@PathVariable long id) {
        try {
            Owner owner = ownerService.findById(id);
            return new ResponseEntity<>(mapper.mapToDto(owner), HttpStatus.OK);
        } catch (OwnerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CREATE
    @PostMapping
    public ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
        Owner owner = mapper.mapToEntity(ownerDTO);
        Owner newOwner = ownerService.create(owner);
        return new ResponseEntity<>(mapper.mapToDto(newOwner), HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<OwnerDTO> update(@PathVariable long id, @RequestBody OwnerDTO ownerDTO) {
        try {
            ownerDTO.setId(id);
            Owner currentOwner = ownerService.findById(id);

            Owner owner = mapper.mapToEntity(ownerDTO);
            Owner updatedOwner = ownerService.update(owner);

            return new ResponseEntity<>(mapper.mapToDto(updatedOwner), HttpStatus.OK);
        } catch (OwnerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            ownerService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OwnerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
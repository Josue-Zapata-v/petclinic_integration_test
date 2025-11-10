package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import com.tecsup.petclinic.mapper.TypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api") // Se agrega el prefijo /api
public class TypeController {

    private TypeService typeService;
    private TypeMapper mapper; // 1. Se declara el mapper

    // 2. Se inyectan ambos, Service y Mapper, en el constructor
    public TypeController(TypeService typeService, TypeMapper mapper){
        this.typeService = typeService;
        this.mapper = mapper;
    }

    // GET all types
    @GetMapping(value = "/types")
    public ResponseEntity<List<TypeDTO>> findAllTypes() {
        // 3. Se usa el mapper inyectado directamente para la conversión
        List<PetType> types = typeService.findAll();
        List<TypeDTO> typesDTO = this.mapper.mapToDtoList(types);

        return ResponseEntity.ok(typesDTO);
    }

    // POST create new type
    @PostMapping(value = "/types")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<TypeDTO> create(@RequestBody TypeDTO typeDTO) {
        TypeDTO newTypeDTO = typeService.create(typeDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newTypeDTO);
    }


    // GET type by ID
    @GetMapping(value = "/types/{id}")
    ResponseEntity<TypeDTO> findById(@PathVariable Integer id) {
        try {
            TypeDTO typeDto = typeService.findById(id);
            return ResponseEntity.ok(typeDto);
        } catch (TypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT update type
    @PutMapping(value = "/types/{id}")
    ResponseEntity<TypeDTO> update(@RequestBody TypeDTO typeDTO, @PathVariable Integer id) {
        try {
            TypeDTO updateTypeDto = typeService.findById(id);

            updateTypeDto.setName(typeDTO.getName());

            typeService.update(updateTypeDto);

            return ResponseEntity.ok(updateTypeDto);

        } catch (TypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE type
    @DeleteMapping(value = "/types/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            typeService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (TypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
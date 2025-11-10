package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper para convertir entre la Entidad Vet y el DTO VetDTO usando MapStruct.
 */
@Mapper(componentModel = "spring")
public interface VetMapper {
    
    VetDTO mapToDto(Vet vet);
    
    Vet mapToEntity(VetDTO vetDTO);
    
    List<VetDTO> mapToDtoList(List<Vet> vets);
    
    List<Vet> mapToEntityList(List<VetDTO> vetDTOs);
}
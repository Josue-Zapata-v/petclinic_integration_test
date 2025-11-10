package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "city", target = "city"),
            @Mapping(source = "telephone", target = "telephone")
    })
    OwnerDTO mapToDto(Owner owner);

    @InheritInverseConfiguration
    Owner mapToEntity(OwnerDTO ownerDTO);

    List<OwnerDTO> mapToDtoList(List<Owner> owners);
    List<Owner> mapToEntityList(List<OwnerDTO> ownerDTOs);
}
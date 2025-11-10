package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy =  NullValueMappingStrategy.RETURN_DEFAULT)
public interface TypeMapper {

    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    PetType mapToEntity(TypeDTO typeDTO);

    TypeDTO mapToDto(PetType petType);

    List<TypeDTO> mapToDtoList(List<PetType> petTypeList);

    List<PetType> mapToEntityList(List<TypeDTO> typeDTOList);
}
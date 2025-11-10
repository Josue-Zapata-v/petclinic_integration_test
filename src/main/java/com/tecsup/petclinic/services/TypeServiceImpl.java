package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mapper.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TypeServiceImpl implements TypeService {

    TypeRepository typeRepository;
    TypeMapper typeMapper;

    public TypeServiceImpl (TypeRepository typeRepository, TypeMapper typeMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }

    @Override
    public TypeDTO create(TypeDTO typeDTO) {
        PetType newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(newType);
    }

    @Override
    public TypeDTO update(TypeDTO typeDTO) {
        PetType updatedType = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(updatedType);
    }

    @Override
    public void delete(Integer id) throws TypeNotFoundException{
        TypeDTO type = findById(id);
        typeRepository.delete(this.typeMapper.mapToEntity(type));
    }

    @Override
    public TypeDTO findById(Integer id) throws TypeNotFoundException {
        Optional<PetType> type = typeRepository.findById(id);

        if ( !type.isPresent())
            throw new TypeNotFoundException("Type not found...!");

        return this.typeMapper.mapToDto(type.get());
    }

    @Override
    public List<TypeDTO> findByName(String name) {
        List<PetType> types = typeRepository.findByName(name);
        return types
                .stream()
                .map(this.typeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetType> findAll() {
        return typeRepository.findAll();
    }
}
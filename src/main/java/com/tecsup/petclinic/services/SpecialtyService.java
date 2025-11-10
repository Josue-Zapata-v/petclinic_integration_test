package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Specialty;

import java.util.List;

public interface SpecialtyService {

    Specialty create(Specialty specialty);

    Specialty update(Integer id, Specialty specialty);

    Specialty findById(Integer id);

    List<Specialty> findAll();

    void delete(Integer id);
}

package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;

import java.util.List;

public interface VisitService {
    Visit create(Visit visit);
    Visit update(Integer id, Visit visit);
    Visit findById(Integer id);
    List<Visit> findAll();
    void delete(Integer id);
}

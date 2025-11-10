package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;

import java.util.List;

public interface OwnerService {

    Owner create(Owner owner);

    Owner update(Long id, Owner owner);

    Owner findById(Long id);

    List<Owner> findAll();

    void delete(Long id);
}

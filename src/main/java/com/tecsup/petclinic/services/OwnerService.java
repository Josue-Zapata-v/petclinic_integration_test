package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import java.util.List;

public interface OwnerService {
    Owner create(Owner owner);
    Owner findById(long id) throws OwnerNotFoundException;
    List<Owner> findAll();
    Owner update(Owner owner);
    void delete(long id) throws OwnerNotFoundException;
}
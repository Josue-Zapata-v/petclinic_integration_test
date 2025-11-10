package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner create(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Owner update(Long id, Owner owner) {
        Owner existing = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        existing.setFirstName(owner.getFirstName());
        existing.setLastName(owner.getLastName());
        existing.setAddress(owner.getAddress());
        existing.setCity(owner.getCity());
        existing.setTelephone(owner.getTelephone());
        return ownerRepository.save(existing);
    }

    @Override
    public Owner findById(Long id) {
        return ownerRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}

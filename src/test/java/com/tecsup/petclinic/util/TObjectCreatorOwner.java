package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import java.util.Arrays;
import java.util.List;

public class TObjectCreatorOwner {

    // --- OWNER ENTITY CREATION ---

    // Owner base (George Franklin)
    public static Owner getOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");
        return owner;
    }

    // New Owner Entity for Creation (Antonio Banderas)
    public static Owner newOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Antonio");
        owner.setLastName("Banderas");
        owner.setAddress("Calle 123");
        owner.setCity("Malaga");
        owner.setTelephone("999888777");
        return owner;
    }

    // New Owner Entity Created (Antonio Banderas with assigned ID)
    public static Owner newOwnerCreated() {
        Owner owner = new Owner();
        owner.setId(300L); // ID simulado
        owner.setFirstName("Antonio");
        owner.setLastName("Banderas");
        owner.setAddress("Calle 123");
        owner.setCity("Malaga");
        owner.setTelephone("999888777");
        return owner;
    }

    // List of Owners from Data.sql (simulated)
    public static List<Owner> getAllOwners() {
        Owner owner1 = getOwner(); // George Franklin

        Owner owner2 = new Owner();
        owner2.setId(2L);
        owner2.setFirstName("Betty");
        owner2.setLastName("Davis");
        owner2.setAddress("638 Cardinal Ave.");
        owner2.setCity("Sun Prairie");
        owner2.setTelephone("6085551749");

        return Arrays.asList(owner1, owner2);
    }

    // --- OWNER DTO CREATION ---

    // Owner DTO base (George Franklin)
    public static OwnerDTO getOwnerDTO() {
        OwnerDTO dto = new OwnerDTO();
        dto.setId(1L);
        dto.setFirstName("George");
        dto.setLastName("Franklin");
        dto.setAddress("110 W. Liberty St.");
        dto.setCity("Madison");
        dto.setTelephone("6085551023");
        return dto;
    }

    // New Owner DTO for Creation (Antonio Banderas)
    public static OwnerDTO newOwnerDTO() {
        OwnerDTO dto = new OwnerDTO();
        dto.setFirstName("Antonio");
        dto.setLastName("Banderas");
        dto.setAddress("Calle 123");
        dto.setCity("Malaga");
        dto.setTelephone("999888777");
        return dto;
    }

    // List of Owner DTOs (simulated)
    public static List<OwnerDTO> getAllOwnerDTOs() {
        OwnerDTO dto1 = getOwnerDTO();

        OwnerDTO dto2 = new OwnerDTO();
        dto2.setId(2L);
        dto2.setFirstName("Betty");
        dto2.setLastName("Davis");
        dto2.setAddress("638 Cardinal Ave.");
        dto2.setCity("Sun Prairie");
        dto2.setTelephone("6085551749");

        return Arrays.asList(dto1, dto2);
    }
}
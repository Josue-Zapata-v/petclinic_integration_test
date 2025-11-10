package com.tecsup.petclinic.dtos;

import lombok.Data;

/**
 * DTO para la entidad Owner (Owner Data Transfer Object).
 */
@Data
public class OwnerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
}
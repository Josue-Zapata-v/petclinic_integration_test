package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para la entidad Vet.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VetDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email; 
    private String phone; 
    private Boolean active; 
}
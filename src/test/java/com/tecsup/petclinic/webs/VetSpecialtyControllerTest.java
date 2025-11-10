package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para la relación Vet-Specialty.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VetSpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Vet vet;
    private Specialty specialty;

    @BeforeEach
    void setup() {
        specialty = new Specialty();
        specialty.setName("Surgery");
        specialty = specialtyRepository.save(specialty);

        vet = new Vet();
        vet.setFirstName("Carlos");
        vet.setLastName("Gomez");
        vet.setSpecialties(new HashSet<>());
        vet.getSpecialties().add(specialty);
        vet = vetRepository.save(vet);
    }

    @Test
    void testFindVetWithSpecialty() throws Exception {
        mockMvc.perform(get("/vets/" + vet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("Gomez")));
    }

    @Test
    void testAddNewSpecialtyToVet() throws Exception {
        Specialty newSpec = new Specialty();
        newSpec.setName("Dentistry");
        newSpec = specialtyRepository.save(newSpec);

        Set<Specialty> specialties = new HashSet<>();
        specialties.add(specialty);
        specialties.add(newSpec);

        vet.setSpecialties(specialties);

        mockMvc.perform(put("/vets/" + vet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vet)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize((int) vetRepository.count())));
    }
}

package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet savedPet;

    @BeforeEach
    void setup() {
        visitRepository.deleteAll();
        petRepository.deleteAll();

        Pet pet = new Pet();
        pet.setName("Firulais");
        pet.setTypeId(1);
        pet.setOwnerId(1);
        pet.setBirthDate(new Date());

        savedPet = petRepository.save(pet);

        objectMapper.addMixIn(Pet.class, IgnoreVisitsMixin.class);
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    abstract class IgnoreVisitsMixin {
        @com.fasterxml.jackson.annotation.JsonIgnore
        abstract Set<Visit> getVisits();
    }

    @Test
    void testCreateVisit() throws Exception {
        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Control médico");
        visit.setPet(savedPet);

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Control médico"));
    }

    @Test
    void testFindVisitById() throws Exception {
        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Consulta general");
        visit.setPet(savedPet);
        Visit savedVisit = visitRepository.save(visit);

        mockMvc.perform(get("/api/visits/" + savedVisit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Consulta general"));
    }

    @Test
    void testUpdateVisit() throws Exception {
        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Dolor pata");
        visit.setPet(savedPet);
        Visit savedVisit = visitRepository.save(visit);

        savedVisit.setDescription("Dolor curado");

        mockMvc.perform(put("/api/visits/" + savedVisit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedVisit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Dolor curado"));
    }

    @Test
    void testDeleteVisit() throws Exception {
        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Eliminar visita");
        visit.setPet(savedPet);
        Visit savedVisit = visitRepository.save(visit);

        mockMvc.perform(delete("/api/visits/" + savedVisit.getId()))
                .andExpect(status().isNoContent());
    }
}

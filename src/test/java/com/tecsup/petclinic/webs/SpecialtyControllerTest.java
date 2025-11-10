package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpecialtyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpecialtyRepository specialtyRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        specialtyRepository.deleteAll();
    }

    @Test
    void testCreate() throws Exception {
        Specialty s = new Specialty();
        s.setName("Neurología");

        mockMvc.perform(post("/api/specialties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(s)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Neurología"));
    }

    @Test
    void testFindById() throws Exception {
        Specialty s = new Specialty();
        s.setName("Cardiología");
        Specialty saved = specialtyRepository.save(s);

        mockMvc.perform(get("/api/specialties/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiología"));
    }

    @Test
    void testUpdate() throws Exception {
        Specialty s = new Specialty();
        s.setName("Dermatología");
        Specialty saved = specialtyRepository.save(s);

        saved.setName("Dermatología Avanzada");

        mockMvc.perform(put("/api/specialties/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dermatología Avanzada"));
    }

    @Test
    void testDelete() throws Exception {
        Specialty s = new Specialty();
        s.setName("Oncología");
        Specialty saved = specialtyRepository.save(s);

        mockMvc.perform(delete("/api/specialties/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}

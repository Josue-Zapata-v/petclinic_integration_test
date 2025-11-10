package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.OwnerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Revierte los cambios de la BD después de cada prueba
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final String BASE_URL = "/owners";

    @Autowired
    private MockMvc mockMvc;

    // Valores esperados del Owner 1 (George Franklin)
    private static final long OWNER_ID = 1L;
    private static final String FIRST_NAME = "George";
    private static final String LAST_NAME = "Franklin";
    private static final String CITY = "Madison";

    // --- LECTURA ---

    @Test
    public void testFindAllOwners() throws Exception {
        this.mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is((int) OWNER_ID)))
                .andExpect(jsonPath("$[0].firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$[0].lastName", is(LAST_NAME)));
    }

    @Test
    public void testFindOwnerOK() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + OWNER_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) OWNER_ID)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.city", is(CITY)));
    }

    @Test
    public void testFindOwnerKO() throws Exception {
        final long ID_NOT_EXIST = 999L;

        mockMvc.perform(get(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }

    // --- CREACIÓN ---

    @Test
    public void testCreateOwner() throws Exception {
        OwnerDTO newOwnerDTO = new OwnerDTO();
        newOwnerDTO.setFirstName("Pedro");
        newOwnerDTO.setLastName("Picapiedra");
        newOwnerDTO.setAddress("Roca 1");
        newOwnerDTO.setCity("Piedradura");
        newOwnerDTO.setTelephone("111222333");

        mockMvc.perform(post(BASE_URL)
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Pedro")))
                .andExpect(jsonPath("$.city", is("Piedradura")));
    }

    // --- ACTUALIZACIÓN ---

    @Test
    public void testUpdateOwnerOK() throws Exception {
        OwnerDTO updateOwnerDTO = new OwnerDTO();
        updateOwnerDTO.setId(OWNER_ID);
        updateOwnerDTO.setFirstName("UPDATED George");
        updateOwnerDTO.setLastName(LAST_NAME);
        updateOwnerDTO.setCity(CITY);
        updateOwnerDTO.setAddress("110 W. Liberty St.");
        updateOwnerDTO.setTelephone("6085551023");

        mockMvc.perform(put(BASE_URL + "/" + OWNER_ID)
                        .content(om.writeValueAsString(updateOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("UPDATED George")));
    }

    @Test
    public void testUpdateOwnerKO() throws Exception {
        final long ID_NOT_EXIST = 999L;

        OwnerDTO updateOwnerDTO = new OwnerDTO();
        updateOwnerDTO.setFirstName("FAIL");
        updateOwnerDTO.setLastName("FAIL");

        mockMvc.perform(put(BASE_URL + "/" + ID_NOT_EXIST)
                        .content(om.writeValueAsString(updateOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // --- ELIMINACIÓN ---

    @Test
    public void testDeleteOwnerOK() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + OWNER_ID))
                .andExpect(status().isOk());

        // Verificar que ya no existe
        mockMvc.perform(get(BASE_URL + "/" + OWNER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOwnerKO() throws Exception {
        final long ID_NOT_EXIST = 999L;

        mockMvc.perform(delete(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }

}
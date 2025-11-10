package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para la entidad Types (PetType).
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypesControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    // Los datos iniciales se encuentran en src/main/resources/data.sql, que tiene 8 tipos.

    /**
     * Test de recuperación de todos los Tipos.
     */
    @Test
    public void testFindAllTypes() throws Exception {

        final int NRO_RECORD_EXPECTED = 8;
        final int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/api/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", is(NRO_RECORD_EXPECTED)))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    /**
     * Test de recuperación de Tipo por ID existente (ID 1: cat).
     */
    @Test
    public void testFindTypeOK() throws Exception {

        String TYPE_NAME = "cat";
        int TYPE_ID = 1;

        this.mockMvc.perform(get("/api/types/" + TYPE_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TYPE_ID)))
                .andExpect(jsonPath("$.name", is(TYPE_NAME)));
    }

    /**
     * Test de recuperación de Tipo por ID no existente.
     */
    @Test
    public void testFindTypeKO() throws Exception {

        mockMvc.perform(get("/api/types/666"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de creación de un nuevo Tipo.
     */
    @Test
    public void testCreateType() throws Exception {

        String NEW_TYPE_NAME = "spider";

        TypeDTO newTypeDTO = TypeDTO.builder()
                .name(NEW_TYPE_NAME)
                .build();

        this.mockMvc.perform(post("/api/types")
                        .content(om.writeValueAsString(newTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NEW_TYPE_NAME)));
    }


    /**
     * Test de eliminación de Tipo.
     */
    @Test
    public void testDeleteType() throws Exception {

        String TYPE_NAME_TO_DELETE = "iguana";

        TypeDTO newTypeDTO = TypeDTO.builder()
                .name(TYPE_NAME_TO_DELETE)
                .build();

        // 1. CREATE: Se crea un nuevo registro para luego eliminarlo
        ResultActions mvcActions = mockMvc.perform(post("/api/types")
                        .content(om.writeValueAsString(newTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Extraemos el ID generado
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // 2. DELETE: Eliminamos el registro
        mockMvc.perform(delete("/api/types/" + id ))
                .andExpect(status().isOk());

        // 3. VALIDATE: Verificamos que ya no exista
        mockMvc.perform(get("/api/types/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de actualización de Tipo.
     */
    @Test
    public void testUpdateType() throws Exception {

        // DATOS DE CREACIÓN INICIAL
        String INIT_NAME = "ant";
        // DATOS DE ACTUALIZACIÓN
        String UP_NAME = "worm";

        TypeDTO newTypeDTO = TypeDTO.builder()
                .name(INIT_NAME)
                .build();

        // 1. CREATE
        ResultActions mvcActions = mockMvc.perform(post("/api/types")
                        .content(om.writeValueAsString(newTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // 2. UPDATE
        TypeDTO upTypeDTO = TypeDTO.builder()
                .id(id)
                .name(UP_NAME)
                .build();

        mockMvc.perform(put("/api/types/"+id)
                        .content(om.writeValueAsString(upTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // 3. FIND
        mockMvc.perform(get("/api/types/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_NAME))); // Verifica el nuevo nombre

        // 4. DELETE (Limpieza)
        mockMvc.perform(delete("/api/types/" + id))
                .andExpect(status().isOk());
    }
}
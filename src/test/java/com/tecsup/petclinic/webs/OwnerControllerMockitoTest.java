package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.services.OwnerService;
import com.tecsup.petclinic.util.TObjectCreatorOwner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para OwnerController usando Mockito para simular OwnerService.
 */
@WebMvcTest(OwnerController.class) // Solo carga la capa web
public class OwnerControllerMockitoTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final String BASE_URL = "/owners";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private OwnerMapper mapper;

    // --- LECTURA ---

    @Test
    public void testFindAllOwners() throws Exception {
        List<Owner> mockOwners = TObjectCreatorOwner.getAllOwners();
        List<OwnerDTO> mockOwnerDTOs = TObjectCreatorOwner.getAllOwnerDTOs();

        Mockito.when(ownerService.findAll())
                .thenReturn(mockOwners);
        Mockito.when(mapper.mapToDtoList(mockOwners))
                .thenReturn(mockOwnerDTOs);

        this.mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(mockOwners.size())))
                .andExpect(jsonPath("$[0].firstName", is(mockOwners.get(0).getFirstName())));
    }

    @Test
    public void testFindOwnerOK() throws Exception {
        Owner mockOwner = TObjectCreatorOwner.getOwner();
        OwnerDTO mockOwnerDTO = TObjectCreatorOwner.getOwnerDTO();

        Mockito.when(ownerService.findById(mockOwner.getId()))
                .thenReturn(mockOwner);
        Mockito.when(mapper.mapToDto(mockOwner))
                .thenReturn(mockOwnerDTO);

        mockMvc.perform(get(BASE_URL + "/" + mockOwner.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockOwner.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(mockOwnerDTO.getFirstName())));
    }

    @Test
    public void testFindOwnerKO() throws Exception {
        final long ID_NOT_EXIST = 666L;

        Mockito.when(this.ownerService.findById(ID_NOT_EXIST))
                .thenThrow(new OwnerNotFoundException("Record not found...!"));

        mockMvc.perform(get(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }

    // --- CREACIÓN ---

    @Test
    public void testCreateOwner() throws Exception {
        OwnerDTO newOwnerDTO = TObjectCreatorOwner.newOwnerDTO();
        Owner mockOwner = TObjectCreatorOwner.newOwner();
        Owner createdOwner = TObjectCreatorOwner.newOwnerCreated();

        // Simulación de DTO de respuesta (tomando datos del DTO de entrada pero con ID)
        OwnerDTO createdOwnerDTO = newOwnerDTO;
        createdOwnerDTO.setId(createdOwner.getId());

        Mockito.when(mapper.mapToEntity(newOwnerDTO))
                .thenReturn(mockOwner);
        Mockito.when(ownerService.create(mockOwner))
                .thenReturn(createdOwner);
        Mockito.when(mapper.mapToDto(createdOwner))
                .thenReturn(createdOwnerDTO);

        mockMvc.perform(post(BASE_URL)
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdOwner.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(createdOwner.getFirstName())));
    }

    // --- ELIMINACIÓN ---

    @Test
    public void testDeleteOwnerOK() throws Exception {
        final long ID_TO_DELETE = 7L;

        // Simular que el servicio puede encontrar y luego eliminar.
        Mockito.when(ownerService.findById(ID_TO_DELETE))
                .thenReturn(TObjectCreatorOwner.getOwner()); // Necesario si OwnerServiceImpl llama a findById antes de delete
        Mockito.doNothing().when(this.ownerService).delete(ID_TO_DELETE);

        mockMvc.perform(delete(BASE_URL + "/" + ID_TO_DELETE))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwnerKO() throws Exception {
        final long ID_NOT_EXIST = 999L;

        Mockito.doThrow(new OwnerNotFoundException("Not found")).when(this.ownerService).delete(ID_NOT_EXIST);

        mockMvc.perform(delete(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }
}
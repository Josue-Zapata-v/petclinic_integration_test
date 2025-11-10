package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.services.VetService;
import com.tecsup.petclinic.util.TObjectCreator;
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
 * Pruebas unitarias para VetController usando Mockito para simular VetService.
 */
@WebMvcTest(VetController.class) 
public class VetControllerMockitoTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final String BASE_URL = "/vets";

    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private VetService vetService;
    
    @MockBean
    private VetMapper mapper;

    // --- LECTURA ---
    
    @Test
    public void testFindAllVets() throws Exception {

        List<Vet> mockVets = TObjectCreator.getAllVets();
        List<VetDTO> mockVetDTOs = TObjectCreator.getAllVetDTOs(); // Usamos DTOs de TObjectCreator
        
        // Simular el servicio
        Mockito.when(vetService.findAll())
                .thenReturn(mockVets);
        
        // Simular el mapper
        Mockito.when(mapper.mapToDtoList(mockVets))
                .thenReturn(mockVetDTOs);


        this.mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(mockVetDTOs.size())))
                .andExpect(jsonPath("$[0].id", is(mockVetDTOs.get(0).getId())));
    }

    @Test
    public void testFindVetOK() throws Exception {
        Vet mockVet = TObjectCreator.getVet();
        VetDTO mockVetDTO = TObjectCreator.getVetDTO();
        
        // Simular el servicio
        Mockito.when(vetService.findById(mockVet.getId()))
                .thenReturn(mockVet);
        
        // Simular el mapper
        Mockito.when(mapper.mapToDto(mockVet))
                .thenReturn(mockVetDTO);


        mockMvc.perform(get(BASE_URL + "/" + mockVet.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockVetDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(mockVetDTO.getFirstName())));
    }

    @Test
    public void testFindVetKO() throws Exception {
        final int ID_NOT_EXIST = 666;

        // Simular que el servicio lanza la excepción
        Mockito.when(this.vetService.findById(ID_NOT_EXIST))
                .thenThrow(new VetNotFoundException("Record not found...!"));

        mockMvc.perform(get(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }
    
    // --- CREACIÓN ---

    @Test
    public void testCreateVet() throws Exception {
        VetDTO newVetDTO = TObjectCreator.newVetDTO(); 
        Vet mockVet = TObjectCreator.newVet(); 
        Vet createdVet = TObjectCreator.newVetCreated(); 
    
        // **CORRECCIÓN AQUÍ:** Usamos el DTO de entrada para construir el DTO de salida
        // y le asignamos el ID simulado.
        VetDTO createdVetDTO = newVetDTO; 
        createdVetDTO.setId(createdVet.getId()); 


        // 1. Simular Mapeo de entrada: DTO -> Entidad
        Mockito.when(mapper.mapToEntity(newVetDTO))
                .thenReturn(mockVet);
    
        // 2. Simular Servicio: Crear Entidad -> Entidad creada
        Mockito.when(vetService.create(mockVet))
                .thenReturn(createdVet);
    
        // 3. Simular Mapeo de salida: Entidad creada -> DTO de respuesta
        Mockito.when(mapper.mapToDto(createdVet))
            .thenReturn(createdVetDTO); 


        mockMvc.perform(post(BASE_URL)
                .content(om.writeValueAsString(newVetDTO))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                // La aserción ahora espera el nombre de Antonio
                .andExpect(jsonPath("$.id", is(createdVet.getId())))
                .andExpect(jsonPath("$.firstName", is(createdVet.getFirstName()))); 
    }   

    // --- ELIMINACIÓN ---

    @Test
    public void testDeleteVetOK() throws Exception {
        final int ID_TO_DELETE = 7;
        
        // Simular que el servicio puede realizar la eliminación (no lanza excepción)
        Mockito.doNothing().when(this.vetService).delete(ID_TO_DELETE);

        mockMvc.perform(delete(BASE_URL + "/" + ID_TO_DELETE))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVetKO() throws Exception {
        final int ID_NOT_EXIST = 999;
        
        // Simular que el servicio lanza la excepción al intentar eliminar
        Mockito.doThrow(new VetNotFoundException("Not found")).when(this.vetService).delete(ID_NOT_EXIST);

        mockMvc.perform(delete(BASE_URL + "/" + ID_NOT_EXIST))
                .andExpect(status().isNotFound());
    }
}
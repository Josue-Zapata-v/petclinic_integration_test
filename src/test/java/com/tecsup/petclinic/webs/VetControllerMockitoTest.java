package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.services.VetService;
import com.tecsup.petclinic.util.TObjectCreator;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@AutoConfigureMockMvc
@SpringBootTest(classes = {VetController.class, VetMapper.class}) // Cargar solo el Controller y Mapper
public class VetControllerMockitoTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final String BASE_URL = "/vets";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VetService vetService;
    
    // Usar el Mapper real (si no está @Autowired correctamente)
    private VetMapper mapper = Mappers.getMapper(VetMapper.class);

	// --- LECTURA ---
    
	@Test
	public void testFindAllVets() throws Exception {

		List<Vet> mockVets = TObjectCreator.getAllVets();
        List<VetDTO> mockVetDTOs = mapper.mapToDtoList(mockVets);
        
        // Simular que el servicio devuelve la lista
		Mockito.when(vetService.findAll())
				.thenReturn(mockVets);

		this.mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(mockVets.size())))
				.andExpect(jsonPath("$[0].id", is(mockVets.get(0).getId())));
	}

	@Test
	public void testFindVetOK() throws Exception {
		Vet mockVet = TObjectCreator.getVet();
        VetDTO mockVetDTO = mapper.mapToDto(mockVet);
        
        // Simular que el servicio encuentra la entidad
		Mockito.when(vetService.findById(mockVet.getId()))
				.thenReturn(mockVet);

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
        Vet newVet = mapper.mapToEntity(newVetDTO);
        
        // Simular el resultado de la creación (con un ID asignado)
        Vet createdVet = TObjectCreator.newVetCreated();

		Mockito.when(vetService.create(newVet))
				.thenReturn(createdVet);
        
        // Nota: La conversión de DTO a Entity y viceversa ocurre en el Controller.
        // Aquí pasamos el DTO de entrada y verificamos el resultado.

		mockMvc.perform(post(BASE_URL)
				.content(om.writeValueAsString(newVetDTO))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
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
package com.tecsup.petclinic.webs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * EVIDENCIAS DE LAS PRUEBAS DE INTEGRACIÓN: VetControllerTest.
 * Pruebas de integración a nivel de controlador para VetController.
 * Utiliza MockMvc y el contexto completo de Spring Boot (incluyendo la base de datos).
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;
	
	private final String BASE_URL = "/vets"; 

	// --- LECTURA ---
	
	/**
	 * Prueba la búsqueda de todos los veterinarios.
	 */
	@Test
	public void testFindAllVets() throws Exception {

		// Basado en data.sql: 6 registros.
		final int NRO_RECORD = 6; 
		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(NRO_RECORD))) // Verifica el tamaño total
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	/**
	 * Prueba la búsqueda de un veterinario existente (ID 1: James Carter).
	 */
	@Test
	public void testFindVetOK() throws Exception {

		String FIRST_NAME = "James";
		String LAST_NAME = "Carter";
		
		this.mockMvc.perform(get(BASE_URL + "/1")) 
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)))
				.andExpect(jsonPath("$.active", is(true)));
	}

	/**
	 * Prueba la búsqueda de un veterinario que no existe.
	 */
	@Test
	public void testFindVetKO() throws Exception {

		mockMvc.perform(get(BASE_URL + "/999"))
				.andExpect(status().isNotFound());
	}
	
	// --- CREACION ---

	/**
	 * Prueba la creación de un nuevo veterinario.
	 */
	@Test
	public void testCreateVet() throws Exception {

		String FIRST_NAME = "Antonio";
		String LAST_NAME = "Banderas";
		String EMAIL = "antonio@petclinic.com";
		
		VetDTO newVetTO = VetDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.email(EMAIL)
				.active(true)
				.build();
        
        // La prueba no puede predecir el ID, solo verifica que sea un 201 y los datos enviados.

		this.mockMvc.perform(post(BASE_URL)
						.content(om.writeValueAsString(newVetTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists()) // Verifica que el ID fue generado
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)));
	}

	// --- ACTUALIZACION Y ELIMINACION ---
	
	/**
	 * Prueba la actualización de un veterinario (Ciclo: Crear -> Actualizar -> Eliminar).
	 */
	@Test
	public void testUpdateVet() throws Exception {

		// 1. Datos iniciales para crear
		String FIRST_NAME = "UpdateVetTest";
		String LAST_NAME = "Original";
		
		// 2. Datos para actualizar
		String UP_FIRST_NAME = "UpdatedVet";

		VetDTO newVetTO = VetDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.build();

		// CREATE (Paso 1: Crear)
		ResultActions mvcActions = mockMvc.perform(post(BASE_URL)
						.content(om.writeValueAsString(newVetTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id"); 

		// UPDATE (Paso 2: Actualizar)
		VetDTO upVetTO = VetDTO.builder()
				.id(id)
				.firstName(UP_FIRST_NAME)
				.lastName(LAST_NAME)
				.build();

		mockMvc.perform(put(BASE_URL + "/" + id)
						.content(om.writeValueAsString(upVetTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)));
		
        // DELETE (Paso 3: Limpiar)
		mockMvc.perform(delete(BASE_URL + "/" + id))
				.andExpect(status().isOk());
	}


	/**
	 * Prueba el ciclo completo de creación y eliminación.
	 */
	@Test
	public void testDeleteVet() throws Exception {

		// 1. Datos para crear un Vet
		VetDTO newVetTO = VetDTO.builder()
				.firstName("ToKill")
				.lastName("Later")
				.build();

		// CREATE (Paso 1: Crear)
		ResultActions mvcActions = mockMvc.perform(post(BASE_URL)
				.content(om.writeValueAsString(newVetTO))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id"); 

		// DELETE (Paso 2: Eliminar)
		mockMvc.perform(delete(BASE_URL + "/" + id ))
				.andExpect(status().isOk());
        
        // FIND KO (Paso 3: Verificar eliminación)
		mockMvc.perform(get(BASE_URL + "/" + id))
				.andExpect(status().isNotFound());
	}

	/**
	 * Prueba la eliminación de un veterinario que no existe.
	 */
	@Test
	public void testDeleteVetKO() throws Exception {
		// Asumimos que el ID 1000 no existe
		mockMvc.perform(delete(BASE_URL + "/" + "1000" ))
				.andExpect(status().isNotFound());
	}
}
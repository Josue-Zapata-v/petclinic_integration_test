package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.PetDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.dtos.VetDTO; 
import com.tecsup.petclinic.entities.Vet; 

import java.util.ArrayList;
import java.util.List;

public class TObjectCreator {

	public static Pet getPet() {
		return new Pet(1,"Leo",1,1, null);
	}

	public static Pet newPet() {
		return new Pet(0,"Punky",1,1, null);
	}

	public static Pet newPetCreated() {
		Pet pet = newPet();
		pet.setId(1000);
		return pet;
	}

	public static Pet newPetForUpdate() {
		return new Pet(0,"Bear",1,1,null);
	}

	public static Pet newPetCreatedForUpdate() {
		Pet pet = newPetForUpdate();
		pet.setId(4000);
		return pet;
	}

	public static Pet newPetForDelete() {
		return new Pet(0,"Bird",1,1, null);
	}

	public static Pet newPetCreatedForDelete() {
		Pet pet = newPetForDelete();
		pet.setId(2000);
		return pet;
	}
	public static List<PetDTO> getAllPetTOs() {
		List<PetDTO> petTOs  = new ArrayList<PetDTO>();
		petTOs.add(new PetDTO(1,"Leo",1,1, "2000-09-07"));
		petTOs.add(new PetDTO(2,"Basil",6,2, "2002-08-06"));
		petTOs.add(new PetDTO(3,"Rosy",2,3, "2001-04-17"));
		petTOs.add(new PetDTO(4,"Jewel",2,3, "2000-03-07"));
		petTOs.add(new PetDTO(5,"Iggy",3,4, "2000-11-30"));
		return petTOs;
	}


	public static List<Pet> getPetsForFindByName() {
		List<Pet> pets  = new ArrayList<Pet>();
		pets.add(new Pet(1,"Leo",1,1, null));
		return pets;
	}

	public static List<Pet> getPetsForFindByTypeId() {
		List<Pet> pets  = new ArrayList<Pet>();
		pets.add(new Pet(9,"Lucky",5,7, null));
		pets.add(new Pet(11,"Freddy",5,9, null));
		return pets;
	}

	public static List<Pet> getPetsForFindByOwnerId() {
		List<Pet> pets  = new ArrayList<Pet>();
		pets.add(new Pet(12,"Lucky",2,10, null));
		pets.add(new Pet(13,"Sly",1,10, null));
		return pets;
	}

	public static PetDTO getPetTO() {
		return new PetDTO(1,"Leo",1,1, "2000-09-07");
	}

	public static PetDTO newPetTO() {
		return new PetDTO(-1,"Beethoven",1,1, "2020-05-20");
	}

	public static PetDTO newPetTOForDelete() {
		return new PetDTO(10000,"Beethoven3",1,1, "2020-05-20");
	}

	// -------------------------------------
    // --- NUEVOS MÉTODOS PARA VET ---
    // -------------------------------------

    // Datos base (James Carter - ID 1) basado en data.sql
    public static Vet getVet() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        // No se incluyen especialidades aquí para simplificar
        return vet;
    }

    // Nuevo Vet sin ID (para crear)
    public static Vet newVet() {
        Vet vet = new Vet();
        vet.setFirstName("Antonio");
        vet.setLastName("Banderas");
        return vet;
    }

    // Nuevo Vet creado con ID simulado (para pruebas de Service)
    public static Vet newVetCreated() {
        Vet vet = newVet();
        vet.setId(3000);
        return vet;
    }

    // DTO base (James Carter - ID 1) con campos completos
    public static VetDTO getVetDTO() {
        return VetDTO.builder()
                .id(1)
                .firstName("James")
                .lastName("Carter")
                .email("james.carter@petclinic.com")
                .phone("6085551234")
                .active(true)
                .build();
    }

    // Nuevo DTO sin ID (para crear)
    public static VetDTO newVetDTO() {
        return VetDTO.builder()
                .id(null)
                .firstName("Antonio")
                .lastName("Banderas")
                .email("antonio.b@petclinic.com")
                .phone("999888777")
                .active(true)
                .build();
    }

    // DTO para la eliminación con un ID simulado
    public static VetDTO newVetDTOForDelete() {
        return VetDTO.builder()
                .id(5000)
                .firstName("Dr")
                .lastName("Kill")
                .active(true)
                .build();
    }
    
    // Lista de Vets (para findAll, basado en data.sql, solo los primeros 5 activos)
    public static List<Vet> getAllVets() {
        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet(1, "James", "Carter", null));
        vets.add(new Vet(2, "Helen", "Leary", null));
        vets.add(new Vet(3, "Linda", "Douglas", null));
        vets.add(new Vet(4, "Rafael", "Ortega", null));
        vets.add(new Vet(5, "Henry", "Stevens", null));
        return vets;
    }

    // Lista de VetDTOs (para pruebas de Mockito)
    public static List<VetDTO> getAllVetDTOs() {
        List<VetDTO> vetDTOs = new ArrayList<>();
        vetDTOs.add(getVetDTO());
        vetDTOs.add(VetDTO.builder().id(2).firstName("Helen").lastName("Leary").build());
        return vetDTOs;
    }

}

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

   public static Vet getVet() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        return vet;
    }

    public static Vet newVet() {
        Vet vet = new Vet();
        vet.setFirstName("Antonio");
        vet.setLastName("Banderas");
        return vet;
    }

    public static Vet newVetCreated() {
        Vet vet = newVet();
        vet.setId(3000);
        return vet;
    }

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

    public static VetDTO newVetDTOForDelete() {
        return VetDTO.builder()
                .id(5000)
                .firstName("Dr")
                .lastName("Kill")
                .active(true)
                .build();
    }
    
    public static List<Vet> getAllVets() {
        List<Vet> vets = new ArrayList<>();
        
        Vet v1 = new Vet(); v1.setId(1); v1.setFirstName("James"); v1.setLastName("Carter");
        Vet v2 = new Vet(); v2.setId(2); v2.setFirstName("Helen"); v2.setLastName("Leary");
        Vet v3 = new Vet(); v3.setId(3); v3.setFirstName("Linda"); v3.setLastName("Douglas");
        Vet v4 = new Vet(); v4.setId(4); v4.setFirstName("Rafael"); v4.setLastName("Ortega");
        Vet v5 = new Vet(); v5.setId(5); v5.setFirstName("Henry"); v5.setLastName("Stevens");
        
        vets.add(v1);
        vets.add(v2);
        vets.add(v3);
        vets.add(v4);
        vets.add(v5);
        
        return vets;
    }

    public static List<VetDTO> getAllVetDTOs() {
        List<VetDTO> vetDTOs = new ArrayList<>();
        vetDTOs.add(getVetDTO());
        vetDTOs.add(VetDTO.builder().id(2).firstName("Helen").lastName("Leary").build());
        return vetDTOs;
    }
}

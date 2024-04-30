package at.codersbay.household.dal;

import at.codersbay.household.model.Pet;

import java.io.IOException;
import java.util.List;

public interface PetDao {

    Pet createPet(Pet pet) throws IOException;

    Pet readPet(int id) throws IOException;

    void updatePet(int id, String type, int quantity) throws IOException;

    void deletePet(int id) throws IOException;

    List<Pet> getAllPets() throws IOException;

}

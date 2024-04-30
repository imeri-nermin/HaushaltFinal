package at.codersbay.household.dal;

import at.codersbay.household.dal.PetDao;
import at.codersbay.household.model.Person;
import at.codersbay.household.model.Pet;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.util.*;

public class PetDaoMysql implements PetDao {
    @Override
    public Pet createPet(Pet pet) throws IOException {

            String checkPersonSql = "SELECT id FROM at.codersbay.household.model.Person WHERE firstname = ? AND lastname = ? ";
            String insertPetSql = "INSERT INTO Haustier (anzahl, tier, person_id) VALUES (?, ?, ?)";

            Connection conn = Person.DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement checkPersonStmt = conn.prepareStatement(checkPersonSql);
                 PreparedStatement insertPetStmt = conn.prepareStatement(insertPetSql, Statement.RETURN_GENERATED_KEYS)) {

                Integer personId = null;

                checkPersonStmt.setString(1, pet.getPerson().getFirstname());
                checkPersonStmt.setString(2, pet.getPerson().getLastname());
                ResultSet rs = checkPersonStmt.executeQuery();
                if (rs.next()) {
                    personId = rs.getInt(1);
                }
                if (personId == null) {
                    throw new NoSuchObjectException("Person mit id: " + personId + " nicht gefunden");
                } else {
                    insertPetStmt.setInt(1, pet.getQuantity());
                    insertPetStmt.setString(2, pet.getType());
                    insertPetStmt.setInt(3, personId);
                    insertPetStmt.executeUpdate();
                    rs = insertPetStmt.getGeneratedKeys();
                    if (rs.next()) {
                        pet.setId(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create pet: " + e.getMessage(), e);
            }

        return pet;

    }

    @Override
    public Pet readPet(int id) throws IOException {
        String sql = "SELECT * FROM haustier WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("tier");
                int quantity = rs.getInt("anzahl");

                Pet pet = new Pet(rs.getInt("id"), type, quantity);
                //System.out.println(pet);
                return pet;
            } else {
                throw new NoSuchObjectException("No pet found with id: " + id);
                //return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePet(int id, String type, int quantity) throws IOException {
        String sql = "UPDATE haustier SET tier = ?, anzahl = ? WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, type);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePet(int id) throws IOException {
        String sql = "DELETE FROM haustier WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pet> getAllPets() throws IOException {
        Map<Integer, Pet> petMap = new HashMap<>();
        List<Pet> pets = new ArrayList<>();

        String sql = "SELECT * FROM haustier h LEFT JOIN person p ON h.person_id = p.id";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Pet pet = new Pet(id, rs.getString("tier"), rs.getInt("anzahl"));
                petMap.put(id, pet);
            }
            pets.addAll(petMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pets;
    }
}

package at.codersbay.household.dal;

import at.codersbay.household.dal.HhDao;
import at.codersbay.household.model.Household;
import at.codersbay.household.model.Person;

import java.rmi.NoSuchObjectException;
import java.sql.*;

import java.io.IOException;
import java.util.*;

public class HhDaoMysql implements HhDao {


    @Override
    public Household createHousehold(Household household) {
        String checkHaushaltSql = "SELECT id FROM Haushalt WHERE streetName = ? AND streetNr = ? AND plz = ? AND city = ?";

        String insertHaushaltSql = "INSERT INTO Haushalt (streetName, streetNr, plz, city) VALUES (?, ?, ?, ?)";


        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement checkAddrStmt = conn.prepareStatement(checkHaushaltSql);
             PreparedStatement insertAddrStmt = conn.prepareStatement(insertHaushaltSql, Statement.RETURN_GENERATED_KEYS)) {

            Integer haushaltId = null;



            // Check if the address exists
            checkAddrStmt.setString(1, household.getStreetName());
            checkAddrStmt.setInt(2, household.getStreetNr());
            checkAddrStmt.setInt(3, household.getPlz());
            checkAddrStmt.setString(4, household.getCity());
            ResultSet rs = checkAddrStmt.executeQuery();
            if (rs.next()) {
                haushaltId = rs.getInt(1);
            } else  {

                // Insert the new address
                insertAddrStmt.setString(1, household.getStreetName());
                insertAddrStmt.setInt(2, household.getStreetNr());
                insertAddrStmt.setInt(3, household.getPlz());
                insertAddrStmt.setString(4, household.getCity());
                insertAddrStmt.executeUpdate();
                rs = insertAddrStmt.getGeneratedKeys();
                if (rs.next()) {
                    household.setId(rs.getInt(1));
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return household;
    }


    @Override
    public Household readHousehold(int id) throws IOException {

        String sql = "SELECT * FROM haushalt WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String streetName = rs.getString("streetname");
                int streetNr = rs.getInt("streetnr");
                int plz = rs.getInt("plz");
                String city = rs.getString("city");

                Household household = new Household(rs.getInt("id"), streetName, streetNr, plz, city);
                //System.out.println(household);
                return household;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void updateHousehold(int id, String streetName, int streetNr, int plz, String city) throws IOException {
        String sql = "UPDATE haushalt SET streetname = ?, streetnr = ?, plz = ?, city = ? WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, streetName);
            pstmt.setInt(2, streetNr);
            pstmt.setInt(3, plz);
            pstmt.setString(4, city);
            pstmt.setInt(5, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void deleteHousehold(int id) throws IOException {
        String sql = "DELETE FROM haushalt WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int deletedRows = pstmt.executeUpdate();
            if (deletedRows > 0) {
                //System.out.println("Successfully removed");
            } else {
                throw new NoSuchObjectException("No household with this ID found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Household> getAllHouseholds() throws IOException {
        List<Household> households = new ArrayList<>();

        String sql = "SELECT h.id as household_id, h.streetName, h.streetNr, h.plz, h.city, p.id as person_id, p.firstname, p.lastname, p.birthdate, p.gender FROM haushalt h LEFT JOIN person p ON h.id = p.haushalt_id";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                households.add(new Household(rs.getInt("household_id"), rs.getString("streetName"), rs.getInt("streetNr"),
                        rs.getInt("plz"), rs.getString("city")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return households;
    }
}

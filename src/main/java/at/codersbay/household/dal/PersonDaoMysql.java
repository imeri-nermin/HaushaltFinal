package at.codersbay.household.dal;

import at.codersbay.household.dal.PersonDao;
import at.codersbay.household.model.Gender;
import at.codersbay.household.model.Person;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PersonDaoMysql implements PersonDao {
    @Override
    public Person createPerson(Person person) throws IOException {
        String insertPersonSql = "INSERT INTO org.nermin.at.codersbay.household.model.Person (firstname, lastname, gender, birthdate, haushalt_id) VALUES (?, ?, ?, ?, ?)";


        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement insertPersonStmt = conn.prepareStatement(insertPersonSql, Statement.RETURN_GENERATED_KEYS)) {


            insertPersonStmt.setString(1, person.getFirstname());
            insertPersonStmt.setString(2, person.getLastname());
            insertPersonStmt.setString(3, person.getGender().toString());
            insertPersonStmt.setDate(4, Date.valueOf(person.getBirthdate()));
            insertPersonStmt.setInt(5, person.getHouseholdId());

            insertPersonStmt.executeUpdate();
            ResultSet rs = insertPersonStmt.getGeneratedKeys();
            if (rs.next()) {
                person = new Person(rs.getInt(1),
                        person.getFirstname(),
                        person.getLastname(),
                        Date.valueOf(person.getBirthdate()).toLocalDate(),
                        person.getGender());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    @Override
    public Person readPerson(int id) throws IOException {

        String sql = "SELECT * FROM person WHERE id = ?";
        Person person = null;

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                LocalDate birthdate = Optional.ofNullable(rs.getObject("birthdate", LocalDate.class)).orElse(null);
                Gender gender = rs.getString("gender") == null ? null : Gender.valueOf(rs.getString("gender"));

                person = new Person(rs.getInt("id"), firstname, lastname, birthdate, gender);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }



    @Override
    public void updatePerson(Person person) throws IOException {
        String sql = "UPDATE person SET firstname = ?, lastname = ?, birthdate = ?, gender = ? WHERE id = ?";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, person.getFirstname());
            pstmt.setString(2, person.getLastname());
            pstmt.setDate(3, Date.valueOf(person.getBirthdate()));
            pstmt.setString(4, person.getGender().name());
            pstmt.setInt(5, person.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePerson(int id) throws IOException {
        String sql = "DELETE FROM person WHERE id = ?";
        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person checkPersonExist(String firstName, String lastName) throws IOException {
        String sql = "SELECT id, firstname, lastname FROM person WHERE firstname = ? AND lastname = ?";
        int id = 0;

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");

                Person person = new Person(id, rs.getString("firstName"), rs.getString("lastName"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> getAllPersons() throws IOException {
        Map<Integer, Person> personMap = new HashMap<>();
        List<Person> persons = new ArrayList<>();

        String sql = "SELECT * FROM person p LEFT JOIN haushalt h ON p.haushalt_id = h.id";

        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int Id = rs.getInt("id");
                personMap.put(Id, new Person(Id, rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getObject("birthdate", LocalDate.class),
                        rs.getString("gender") == null ? null : Gender.valueOf(rs.getString("gender"))));
            }
            persons.addAll(personMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return persons;
    }

}




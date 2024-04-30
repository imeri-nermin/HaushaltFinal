package at.codersbay.household.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Person {
    private int id;
    private String firstname;
    private String lastname;
    private Gender gender;
    private LocalDate birthdate;
    private Household household;
    private List<Pet> pets;
    private int householdId;

    public Person(String firstname, String lastname, LocalDate birthdate, Gender gender, int householdId) {
        setFirstname(firstname);
        setLastname(lastname);
        setBirthdate(birthdate);
        setGender(gender);
        setHouseholdId(householdId);
    }

    public Person(int id, String firstname, String lastname, LocalDate birthdate, Gender gender) {
        setId(id);
        setFirstname(firstname);
        setLastname(lastname);
        setBirthdate(birthdate);
        setGender(gender);
    }

    public Person(int id, String firstname, String lastname) {
        setId(id);
        setFirstname(firstname);
        setLastname(lastname);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (firstname == null || firstname.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if (lastname == null || lastname.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    @Override
    public String toString() {
        String string = "ID = " + id + ", firstname = " + firstname + ", lastname = " + lastname;

        if (gender != null) {
            string += ", gender = " + gender;
        }

        if (birthdate != null) {
            string += ", birthdate = " + birthdate;
        }

        /*if (household != null) {
            string += ", address = " + household;
        }*/

        return string;
    }

    public static class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/haushalt";
        private static final String USER = "root";
        private static final String PASSWORD = "";
        private static DatabaseConnection instance;
        private static Connection connection;

      /*  public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } */

        private DatabaseConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        }

        public static DatabaseConnection getInstance() {
            try {
                if (instance == null || connection == null || connection.isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return instance;
        }

        public Connection getConnection() {
            return connection;
        }
    }
}

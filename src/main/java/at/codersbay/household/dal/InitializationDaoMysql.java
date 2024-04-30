package at.codersbay.household.dal;
import at.codersbay.household.model.Person;
import at.codersbay.household.dal.InitializationDao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InitializationDaoMysql implements InitializationDao {
    @Override
    public void createTables() throws IOException {
        String createHaushaltTableSQL = "CREATE TABLE IF NOT EXISTS haushalt (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "streetName VARCHAR(255) NULL, " +
                "streetNr INT NULL, " +
                "plz INT NULL, " +
                "city VARCHAR(255) NULL);";

        String createPersonTableSQL = "CREATE TABLE IF NOT EXISTS person (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "firstname VARCHAR(255) NULL, " +
                "lastname VARCHAR(255) NULL, " +
                "birthdate DATE NULL, " +
                "gender ENUM('male', 'female') NULL, " +
                "haushalt_id INT NULL, " +
                "CONSTRAINT fk_person_haushalt FOREIGN KEY (haushalt_id) REFERENCES haushalt(id) ON DELETE SET NULL ON UPDATE CASCADE);";

        String createHaustierTableSQL = "CREATE TABLE IF NOT EXISTS haustier (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "tier VARCHAR(255), " +
                "anzahl INT, " +
                "person_id INT NULL, " +
                "CONSTRAINT fk_haustier_person FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE SET NULL ON UPDATE CASCADE);";


        try (Connection conn = Person.DatabaseConnection.getInstance().getConnection();
             PreparedStatement createHaushaltTableStmt = conn.prepareStatement(createHaushaltTableSQL);
             PreparedStatement createPersonTableStmt = conn.prepareStatement(createPersonTableSQL);
             PreparedStatement createHaustierTableStmt = conn.prepareStatement(createHaustierTableSQL)) {
            createHaushaltTableStmt.executeUpdate();
            createPersonTableStmt.executeUpdate();
            createHaustierTableStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
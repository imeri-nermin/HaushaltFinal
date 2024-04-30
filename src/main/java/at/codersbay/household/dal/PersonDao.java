package at.codersbay.household.dal;

import at.codersbay.household.model.Person;

import java.io.IOException;
import java.util.List;

public interface PersonDao {

        Person createPerson(Person person) throws IOException;

        Person readPerson(int id) throws IOException;

        void updatePerson(Person person) throws IOException;

        void deletePerson(int id) throws IOException;

        Person checkPersonExist(String firstName, String lastname) throws IOException;

        List<Person> getAllPersons() throws IOException;
    }


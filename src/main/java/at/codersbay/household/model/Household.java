package at.codersbay.household.model;

import java.io.IOException;
import java.util.List;

public class Household {
    private int id;
    private String streetName;
    private int streetNr;
    private int plz;
    private String city;
    private List<Person> persons;

    public Household(int id, String streetName, int streetNr, int plz, String city) throws IOException {
        setId(id);
        setStreetName(streetName);
        setStreetNr(streetNr);
        setPlz(plz);
        setCity(city);
    }

    public Household(String streetName, int streetNr, int plz, String city) throws IOException {
        setStreetName(streetName);
        setStreetNr(streetNr);
        setPlz(plz);
        setCity(city);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) throws IllegalArgumentException {
        if (streetName == null || streetName.trim().isEmpty()) {
            throw new IllegalArgumentException("Street name cannot be null or empty.");
        }
        this.streetName = streetName;
    }

    public int getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(int streetNr) {
        if (streetNr <= 0) {
            throw new IllegalArgumentException("Street number must be a positive integer.");
        }
        this.streetNr = streetNr;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        if (plz <= 0) {
            throw new IllegalArgumentException("PLZ must be a positive integer.");
        }
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) throws IllegalArgumentException {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty.");
        }
        this.city = city;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "ID:" + id + " Street-Name = " + streetName + ", Streetnr. = " + streetNr + ", PLZ = " + plz + ", Town = " + city;
    }
}

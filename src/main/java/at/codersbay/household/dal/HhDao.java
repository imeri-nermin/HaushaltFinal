package at.codersbay.household.dal;

import at.codersbay.household.model.Household;

import java.io.IOException;
import java.util.List;

public interface HhDao {


    Household createHousehold(Household haushalt) throws IOException;

    Household readHousehold(int id) throws IOException;

    void updateHousehold(int id, String streetName, int streetNr, int plz, String city) throws IOException;

    void deleteHousehold(int id) throws IOException;

    List<Household> getAllHouseholds() throws IOException;

}

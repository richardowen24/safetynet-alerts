package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void getPersons_shouldLoadRealDataFromJson() {
        assertFalse(dataRepository.getPersons().isEmpty());
    }

    @Test
    public void getFireStations_shouldLoadRealDataFromJson() {
        assertFalse(dataRepository.getFireStations().isEmpty());
    }

    @Test
    public void getMedicalRecords_shouldLoadRealDataFromJson() {
        assertFalse(dataRepository.getMedicalRecords().isEmpty());
    }

    @Test
    public void addPerson_shouldIncreasePersonCount() {
        int before = dataRepository.getPersons().size();

        Person newPerson = new Person();
        newPerson.setFirstName("Test");
        newPerson.setLastName("AddPerson");
        dataRepository.addPerson(newPerson);

        int after = dataRepository.getPersons().size();
        assertEquals(before + 1, after);

        dataRepository.deletePerson("Test", "AddPerson");
    }

    @Test
    public void updatePerson_shouldReturnTrue_whenPersonExists() {
        Person updated = new Person();
        updated.setFirstName("Test");
        updated.setLastName("UpdatePerson");
        updated.setAddress("Original Address");
        dataRepository.addPerson(updated);

        updated.setAddress("New Address");
        boolean result = dataRepository.updatePerson(updated);

        assertTrue(result);

        dataRepository.deletePerson("Test", "UpdatePerson");
    }

    @Test
    public void updatePerson_shouldReturnFalse_whenPersonDoesNotExist() {
        Person nonExistent = new Person();
        nonExistent.setFirstName("Nobody");
        nonExistent.setLastName("Nowhere");

        boolean result = dataRepository.updatePerson(nonExistent);

        assertFalse(result);
    }

    @Test
    public void deletePerson_shouldReturnTrue_whenPersonExists() {
        Person toDelete = new Person();
        toDelete.setFirstName("Test");
        toDelete.setLastName("DeletePerson");
        dataRepository.addPerson(toDelete);

        boolean result = dataRepository.deletePerson("Test", "DeletePerson");

        assertTrue(result);
    }

    @Test
    public void addFireStation_shouldIncreaseFireStationCount() {
        int before = dataRepository.getFireStations().size();

        FireStation newStation = new FireStation();
        newStation.setAddress("Test Address Repo");
        newStation.setStation("99");
        dataRepository.addFireStation(newStation);

        int after = dataRepository.getFireStations().size();
        assertEquals(before + 1, after);

        dataRepository.deleteFireStationByAddress("Test Address Repo");
    }

    @Test
    public void updateFireStation_shouldReturnTrue_whenAddressExists() {
        FireStation station = new FireStation();
        station.setAddress("Test Address Update Repo");
        station.setStation("1");
        dataRepository.addFireStation(station);

        boolean result = dataRepository.updateFireStation("Test Address Update Repo", "2");

        assertTrue(result);

        dataRepository.deleteFireStationByAddress("Test Address Update Repo");
    }

    @Test
    public void deleteFireStationByAddress_shouldReturnTrue_whenAddressExists() {
        FireStation station = new FireStation();
        station.setAddress("Test Address Delete Repo");
        station.setStation("1");
        dataRepository.addFireStation(station);

        boolean result = dataRepository.deleteFireStationByAddress("Test Address Delete Repo");

        assertTrue(result);
    }

    @Test
    public void addMedicalRecord_shouldIncreaseMedicalRecordCount() {
        int before = dataRepository.getMedicalRecords().size();

        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Test");
        record.setLastName("AddRecord");
        dataRepository.addMedicalRecord(record);

        int after = dataRepository.getMedicalRecords().size();
        assertEquals(before + 1, after);

        dataRepository.deleteMedicalRecord("Test", "AddRecord");
    }

    @Test
    public void updateMedicalRecord_shouldReturnTrue_whenRecordExists() {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Test");
        record.setLastName("UpdateRecord");
        record.setBirthdate("01/01/1990");
        dataRepository.addMedicalRecord(record);

        boolean result = dataRepository.updateMedicalRecord(record);

        assertTrue(result);

        dataRepository.deleteMedicalRecord("Test", "UpdateRecord");
    }

    @Test
    public void deleteMedicalRecord_shouldReturnTrue_whenRecordExists() {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Test");
        record.setLastName("DeleteRecord");
        dataRepository.addMedicalRecord(record);

        boolean result = dataRepository.deleteMedicalRecord("Test", "DeleteRecord");

        assertTrue(result);
    }
}
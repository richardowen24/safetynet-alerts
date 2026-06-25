package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FireStationServiceTest {

    private DataRepository mockRepository;
    private PersonService mockPersonService;
    private FireStationService fireStationService;
    private Person john;
    private FireStation station1;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(DataRepository.class);
        mockPersonService = mock(PersonService.class);
        AgeCalculator ageCalculator = new AgeCalculator();
        fireStationService = new FireStationService(mockRepository, ageCalculator, mockPersonService);

        john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAddress("1509 Culver St");
        john.setPhone("841-874-6512");

        station1 = new FireStation();
        station1.setAddress("1509 Culver St");
        station1.setStation("3");
    }

    @Test
    public void getAddressesByStation_shouldReturnMatchingAddress_whenStationMatches() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));

        List<String> result = fireStationService.getAddressesByStation("3");

        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0));
    }

    @Test
    public void getStationByAddress_shouldReturnCorrectStation_whenAddressMatches() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));

        String result = fireStationService.getStationByAddress("1509 Culver St");

        assertEquals("3", result);
    }

    @Test
    public void getPersonsByStation_shouldReturnPerson_whenStationCoversTheirAddress() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));
        when(mockPersonService.getPersonsByAddress("1509 Culver St")).thenReturn(List.of(john));

        List<Person> result = fireStationService.getPersonsByStation("3");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void countChildrenByStation_shouldCountCorrectly_whenPersonIsAChild() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));
        when(mockPersonService.getPersonsByAddress("1509 Culver St")).thenReturn(List.of(john));

        MedicalRecord childRecord = new MedicalRecord();
        childRecord.setFirstName("John");
        childRecord.setLastName("Boyd");
        childRecord.setBirthdate("03/06/2020");

        when(mockPersonService.getMedicalRecordForPerson(john)).thenReturn(Optional.of(childRecord));

        long childCount = fireStationService.countChildrenByStation("3");
        long adultCount = fireStationService.countAdultsByStation("3");

        assertEquals(1, childCount);
        assertEquals(0, adultCount);
    }

    @Test
    public void getPhoneNumbersByStation_shouldReturnDistinctPhones() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));
        when(mockPersonService.getPersonsByAddress("1509 Culver St")).thenReturn(List.of(john));

        List<String> result = fireStationService.getPhoneNumbersByStation("3");

        assertEquals(1, result.size());
        assertEquals("841-874-6512", result.get(0));
    }

    @Test
    public void getResidentsByAddress_shouldReturnCorrectStationAndResidents() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));
        when(mockPersonService.getPersonsByAddress("1509 Culver St")).thenReturn(List.of(john));
        when(mockPersonService.getMedicalRecordForPerson(john)).thenReturn(Optional.empty());

        FireAddressDTO result = fireStationService.getResidentsByAddress("1509 Culver St");

        assertEquals("3", result.getStationNumber());
        assertEquals(1, result.getResidents().size());
        assertEquals("John", result.getResidents().get(0).getFirstName());
    }

    @Test
    public void getHouseholdsByStations_shouldReturnGroupedAddresses() {
        when(mockRepository.getFireStations()).thenReturn(List.of(station1));
        when(mockPersonService.getPersonsByAddress("1509 Culver St")).thenReturn(List.of(john));
        when(mockPersonService.getMedicalRecordForPerson(john)).thenReturn(Optional.empty());

        List<FloodAddressDTO> result = fireStationService.getHouseholdsByStations(List.of("3"));

        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0).getAddress());
    }

    @Test
    public void addFireStation_shouldReturnSavedStation() {
        FireStation result = fireStationService.addFireStation(station1);

        assertEquals("1509 Culver St", result.getAddress());
    }

    @Test
    public void updateFireStation_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.updateFireStation("1509 Culver St", "5")).thenReturn(true);

        boolean result = fireStationService.updateFireStation("1509 Culver St", "5");

        assertTrue(result);
    }

    @Test
    public void deleteFireStationByAddress_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.deleteFireStationByAddress("1509 Culver St")).thenReturn(true);

        boolean result = fireStationService.deleteFireStationByAddress("1509 Culver St");

        assertTrue(result);
    }
}
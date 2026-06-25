package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    private DataRepository mockRepository;
    private PersonService personService;
    private Person john;
    private Person jacob;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(DataRepository.class);
        AgeCalculator ageCalculator = new AgeCalculator();
        personService = new PersonService(mockRepository, ageCalculator);

        john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAddress("1509 Culver St");
        john.setCity("Culver");
        john.setEmail("jaboyd@email.com");

        jacob = new Person();
        jacob.setFirstName("Jacob");
        jacob.setLastName("Boyd");
        jacob.setAddress("1509 Culver St");
        jacob.setCity("Culver");
        jacob.setEmail("drk@email.com");
    }

    @Test
    public void getPersonsByAddress_shouldReturnMatchingPerson_whenAddressMatches() {
        when(mockRepository.getPersons()).thenReturn(List.of(john));

        List<Person> result = personService.getPersonsByAddress("1509 Culver St");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void getPersonsByAddress_shouldReturnEmptyList_whenNoAddressMatches() {
        when(mockRepository.getPersons()).thenReturn(List.of(john));

        List<Person> result = personService.getPersonsByAddress("Nonexistent Address");

        assertEquals(0, result.size());
    }

    @Test
    public void getChildAlertsByAddress_shouldReturnChild_withHouseholdMembers() {
        MedicalRecord childRecord = new MedicalRecord();
        childRecord.setFirstName("John");
        childRecord.setLastName("Boyd");
        childRecord.setBirthdate("03/06/2020");

        when(mockRepository.getPersons()).thenReturn(List.of(john, jacob));
        when(mockRepository.getMedicalRecords()).thenReturn(List.of(childRecord));

        List<ChildAlertDTO> result = personService.getChildAlertsByAddress("1509 Culver St");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals(1, result.get(0).getHouseholdMembers().size());
        assertEquals("Jacob", result.get(0).getHouseholdMembers().get(0).getFirstName());
    }

    @Test
    public void getPersonInfoByLastName_shouldReturnCorrectInfo() {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("John");
        record.setLastName("Boyd");
        record.setBirthdate("03/06/1984");
        record.setMedications(List.of());
        record.setAllergies(List.of());

        when(mockRepository.getPersons()).thenReturn(List.of(john));
        when(mockRepository.getMedicalRecords()).thenReturn(List.of(record));

        List<PersonInfoDTO> result = personService.getPersonInfoByLastName("Boyd");

        assertEquals(1, result.size());
        assertEquals(42, result.get(0).getAge());
        assertEquals("jaboyd@email.com", result.get(0).getEmail());
    }

    @Test
    public void getEmailsByCity_shouldReturnEmail_whenCityMatches() {
        when(mockRepository.getPersons()).thenReturn(List.of(john));

        List<String> result = personService.getEmailsByCity("Culver");

        assertEquals(1, result.size());
        assertEquals("jaboyd@email.com", result.get(0));
    }

    @Test
    public void addPerson_shouldReturnSavedPerson() {
        Person result = personService.addPerson(john);

        assertEquals("John", result.getFirstName());
    }

    @Test
    public void updatePerson_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.updatePerson(john)).thenReturn(true);

        boolean result = personService.updatePerson(john);

        assertTrue(result);
    }

    @Test
    public void deletePerson_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.deletePerson("John", "Boyd")).thenReturn(true);

        boolean result = personService.deletePerson("John", "Boyd");

        assertTrue(result);
    }
}
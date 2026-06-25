package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    private DataRepository mockRepository;
    private PersonService personService;
    private Person john;

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

}

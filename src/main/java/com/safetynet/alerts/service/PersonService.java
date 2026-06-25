package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public PersonService(DataRepository dataRepository, AgeCalculator ageCalculator) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public List<Person> getPersonsByAddress(String address) {
        return dataRepository.getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public List<Person> getPersonsByLastName(String lastName) {
        return dataRepository.getPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public List<Person> getPersonsByCity(String city) {
        return dataRepository.getPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public Optional<MedicalRecord> getMedicalRecordForPerson(Person person) {
        return dataRepository.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                                && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
    }

    }
    
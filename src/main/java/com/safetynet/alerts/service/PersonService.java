package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import java.util.ArrayList;
import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
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

    public List<ChildAlertDTO> getChildAlertsByAddress(String address) {
        List<Person> residents = getPersonsByAddress(address);
        List<ChildAlertDTO> childAlerts = new java.util.ArrayList<>();

        for (Person person : residents) {
            Optional<MedicalRecord> medicalRecord = getMedicalRecordForPerson(person);

            if (medicalRecord.isPresent()) {
                int age = ageCalculator.calculateAge(medicalRecord.get().getBirthdate());

                if (ageCalculator.isChild(age)) {
                    List<HouseholdMemberDTO> householdMembers = residents.stream()
                            .filter(otherPerson -> !(otherPerson.getFirstName().equals(person.getFirstName())
                                                   && otherPerson.getLastName().equals(person.getLastName())))
                            .map(otherPerson -> new HouseholdMemberDTO(otherPerson.getFirstName(), otherPerson.getLastName()))
                            .collect(Collectors.toList());

                    childAlerts.add(new ChildAlertDTO(person.getFirstName(), person.getLastName(), age, householdMembers));
                }
            }
        }

        return childAlerts;
    }
    List<ChildAlertDTO> childAlerts = new ArrayList<>();

    public List<String> getEmailsByCity(String city) {
        return getPersonsByCity(city).stream()
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        List<Person> persons = getPersonsByLastName(lastName);
        List<PersonInfoDTO> personInfos = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecord = getMedicalRecordForPerson(person);

            int age = 0;
            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (medicalRecord.isPresent()) {
                age = ageCalculator.calculateAge(medicalRecord.get().getBirthdate());
                medications = medicalRecord.get().getMedications();
                allergies = medicalRecord.get().getAllergies();
            }

            personInfos.add(new PersonInfoDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    age,
                    person.getEmail(),
                    medications,
                    allergies));
        }

        return personInfos;
    }
    }

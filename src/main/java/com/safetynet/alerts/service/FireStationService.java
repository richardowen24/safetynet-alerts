package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FireResidentDTO;
import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.model.MedicalRecord;
import java.util.ArrayList;
import java.util.Optional;
import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FireResidentDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;
    private final PersonService personService;

    public FireStationService(DataRepository dataRepository, AgeCalculator ageCalculator, PersonService personService) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
        this.personService = personService;
    }

    public List<String> getAddressesByStation(String stationNumber) {
        return dataRepository.getFireStations().stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }

    public String getStationByAddress(String address) {
        return dataRepository.getFireStations().stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .map(FireStation::getStation)
                .findFirst()
                .orElse(null);
    }

    public List<Person> getPersonsByStation(String stationNumber) {
        List<String> addresses = getAddressesByStation(stationNumber);

        return addresses.stream()
                .flatMap(address -> personService.getPersonsByAddress(address).stream())
                .collect(Collectors.toList());
    }

    public long countAdultsByStation(String stationNumber) {
        return getPersonsByStation(stationNumber).stream()
                .filter(person -> !isChildPerson(person))
                .count();
    }

    public long countChildrenByStation(String stationNumber) {
        return getPersonsByStation(stationNumber).stream()
                .filter(this::isChildPerson)
                .count();
    }

    private boolean isChildPerson(Person person) {
        return personService.getMedicalRecordForPerson(person)
                .map(record -> ageCalculator.calculateAge(record.getBirthdate()))
                .map(ageCalculator::isChild)
                .orElse(false);
    }

    public List<String> getPhoneNumbersByStation(String stationNumber) {
        return getPersonsByStation(stationNumber).stream()
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }
public FireAddressDTO getResidentsByAddress(String address) {
        String stationNumber = getStationByAddress(address);
        List<Person> residents = personService.getPersonsByAddress(address);
        List<FireResidentDTO> fireResidents = buildFireResidentList(residents);

        return new FireAddressDTO(stationNumber, fireResidents);
    }

    private List<FireResidentDTO> buildFireResidentList(List<Person> persons) {
        List<FireResidentDTO> fireResidents = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecord = personService.getMedicalRecordForPerson(person);

            int age = 0;
            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (medicalRecord.isPresent()) {
                age = ageCalculator.calculateAge(medicalRecord.get().getBirthdate());
                medications = medicalRecord.get().getMedications();
                allergies = medicalRecord.get().getAllergies();
            }

            fireResidents.add(new FireResidentDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getPhone(),
                    age,
                    medications,
                    allergies));
        }

        return fireResidents;
    }
    public List<FloodAddressDTO> getHouseholdsByStations(List<String> stationNumbers) {
        List<String> addresses = new ArrayList<>();

        for (String stationNumber : stationNumbers) {
            addresses.addAll(getAddressesByStation(stationNumber));
        }

        List<String> uniqueAddresses = addresses.stream()
                .distinct()
                .collect(Collectors.toList());

        List<FloodAddressDTO> floodAddresses = new ArrayList<>();

        for (String address : uniqueAddresses) {
            List<Person> residents = personService.getPersonsByAddress(address);
            List<FireResidentDTO> fireResidents = buildFireResidentList(residents);
            floodAddresses.add(new FloodAddressDTO(address, fireResidents));
        }

        return floodAddresses;
    }

    }

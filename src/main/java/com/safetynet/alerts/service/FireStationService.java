package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.stereotype.Service;

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

    }
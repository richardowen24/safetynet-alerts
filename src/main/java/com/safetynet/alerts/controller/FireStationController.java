package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FireStationCoverageDTO;
import com.safetynet.alerts.dto.PersonSummaryDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/firestation")
    public FireStationCoverageDTO getPersonsByStation(@RequestParam String stationNumber) {
        List<Person> persons = fireStationService.getPersonsByStation(stationNumber);

        List<PersonSummaryDTO> personSummaries = persons.stream()
                .map(person -> new PersonSummaryDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()))
                .collect(Collectors.toList());

        long childCount = fireStationService.countChildrenByStation(stationNumber);
        long adultCount = fireStationService.countAdultsByStation(stationNumber);

        return new FireStationCoverageDTO(personSummaries, adultCount, childCount);
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneAlert(@RequestParam String firestation) {
        return fireStationService.getPhoneNumbersByStation(firestation);
    }

    @GetMapping("/fire")
    public FireAddressDTO getFireInfo(@RequestParam String address) {
        return fireStationService.getResidentsByAddress(address);
    }

    @GetMapping("/flood/stations")
    public List<FloodAddressDTO> getFloodStations(@RequestParam List<String> stations) {
        return fireStationService.getHouseholdsByStations(stations);
    }
}
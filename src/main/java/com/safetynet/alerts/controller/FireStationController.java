package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FireStationCoverageDTO;
import com.safetynet.alerts.dto.PersonSummaryDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FireStationController {

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/firestation")
    public FireStationCoverageDTO getPersonsByStation(@RequestParam String stationNumber) {
        logger.debug("Looking up persons covered by station number: {}", stationNumber);
        try {
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

            logger.info("Returned {} persons for station {} ({} adults, {} children)",
                    personSummaries.size(), stationNumber, adultCount, childCount);

            return new FireStationCoverageDTO(personSummaries, adultCount, childCount);
        } catch (Exception e) {
            logger.error("Error retrieving persons for station {}: {}", stationNumber, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneAlert(@RequestParam String firestation) {
        logger.debug("Looking up phone numbers for station: {}", firestation);
        try {
            List<String> phones = fireStationService.getPhoneNumbersByStation(firestation);
            logger.info("Returned {} phone numbers for station {}", phones.size(), firestation);
            return phones;
        } catch (Exception e) {
            logger.error("Error retrieving phone numbers for station {}: {}", firestation, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/fire")
    public FireAddressDTO getFireInfo(@RequestParam String address) {
        logger.debug("Looking up fire info for address: {}", address);
        try {
            FireAddressDTO result = fireStationService.getResidentsByAddress(address);
            logger.info("Returned fire info for address {} (station {})", address, result.getStationNumber());
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving fire info for address {}: {}", address, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/flood/stations")
    public List<FloodAddressDTO> getFloodStations(@RequestParam List<String> stations) {
        logger.debug("Looking up households for stations: {}", stations);
        try {
            List<FloodAddressDTO> result = fireStationService.getHouseholdsByStations(stations);
            logger.info("Returned {} addresses for stations {}", result.size(), stations);
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving households for stations {}: {}", stations, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/firestation")
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        logger.debug("Adding fire station mapping: {}", fireStation);
        try {
            FireStation result = fireStationService.addFireStation(fireStation);
            logger.info("Added fire station mapping for address {}", result.getAddress());
            return result;
        } catch (Exception e) {
            logger.error("Error adding fire station mapping: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/firestation")
    public boolean updateFireStation(@RequestParam String address, @RequestParam String station) {
        logger.debug("Updating fire station mapping for address: {}", address);
        try {
            boolean updated = fireStationService.updateFireStation(address, station);
            logger.info("Update fire station mapping for address {}: {}", address, updated);
            return updated;
        } catch (Exception e) {
            logger.error("Error updating fire station mapping for address {}: {}", address, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/firestation")
    public boolean deleteFireStation(@RequestParam String address) {
        logger.debug("Deleting fire station mapping for address: {}", address);
        try {
            boolean deleted = fireStationService.deleteFireStationByAddress(address);
            logger.info("Delete fire station mapping for address {}: {}", address, deleted);
            return deleted;
        } catch (Exception e) {
            logger.error("Error deleting fire station mapping for address {}: {}", address, e.getMessage());
            throw e;
        }
    }
}
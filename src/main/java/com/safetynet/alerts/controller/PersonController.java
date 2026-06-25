package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
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

@RestController
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        logger.debug("Looking up children at address: {}", address);
        try {
            List<ChildAlertDTO> result = personService.getChildAlertsByAddress(address);
            logger.info("Returned {} children for address {}", result.size(), address);
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving children for address {}: {}", address, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        logger.debug("Looking up emails for city: {}", city);
        try {
            List<String> emails = personService.getEmailsByCity(city);
            logger.info("Returned {} emails for city {}", emails.size(), city);
            return emails;
        } catch (Exception e) {
            logger.error("Error retrieving emails for city {}: {}", city, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {
        logger.debug("Looking up person info for last name: {}", lastName);
        try {
            List<PersonInfoDTO> result = personService.getPersonInfoByLastName(lastName);
            logger.info("Returned {} person info records for last name {}", result.size(), lastName);
            return result;
        } catch (Exception e) {
            logger.error("Error retrieving person info for last name {}: {}", lastName, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        logger.debug("Adding person: {}", person);
        try {
            Person result = personService.addPerson(person);
            logger.info("Added person {} {}", result.getFirstName(), result.getLastName());
            return result;
        } catch (Exception e) {
            logger.error("Error adding person: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/person")
    public boolean updatePerson(@RequestBody Person person) {
        logger.debug("Updating person: {} {}", person.getFirstName(), person.getLastName());
        try {
            boolean updated = personService.updatePerson(person);
            logger.info("Update person {} {}: {}", person.getFirstName(), person.getLastName(), updated);
            return updated;
        } catch (Exception e) {
            logger.error("Error updating person {} {}: {}", person.getFirstName(), person.getLastName(), e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/person")
    public boolean deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.debug("Deleting person: {} {}", firstName, lastName);
        try {
            boolean deleted = personService.deletePerson(firstName, lastName);
            logger.info("Delete person {} {}: {}", firstName, lastName, deleted);
            return deleted;
        } catch (Exception e) {
            logger.error("Error deleting person {} {}: {}", firstName, lastName, e.getMessage());
            throw e;
        }
    }
}
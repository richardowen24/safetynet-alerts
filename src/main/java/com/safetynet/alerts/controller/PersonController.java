package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        return personService.getChildAlertsByAddress(address);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {
        return personService.getPersonInfoByLastName(lastName);
    }

}
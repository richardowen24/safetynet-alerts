package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    public void getChildAlert_shouldReturn200AndChildren() throws Exception {
        ChildAlertDTO child = new ChildAlertDTO("Tenley", "Boyd", 14, List.of());
        when(personService.getChildAlertsByAddress("1509 Culver St")).thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$[0].age").value(14));
    }

    @Test
    public void getCommunityEmail_shouldReturn200AndEmails() throws Exception {
        when(personService.getEmailsByCity("Culver")).thenReturn(List.of("jaboyd@email.com"));

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("jaboyd@email.com"));
    }

    @Test
    public void getPersonInfo_shouldReturn200AndPersonData() throws Exception {
        PersonInfoDTO info = new PersonInfoDTO("John", "Boyd", "1509 Culver St", 42, "jaboyd@email.com", List.of(), List.of());
        when(personService.getPersonInfoByLastName("Boyd")).thenReturn(List.of(info));

        mockMvc.perform(get("/personInfo").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(42));
    }

    @Test
    public void addPerson_shouldReturn200AndCreatedPerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Person");

        when(personService.addPerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"Person\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    public void updatePerson_shouldReturnTrue() throws Exception {
        when(personService.updatePerson(any(Person.class))).thenReturn(true);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"Person\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void deletePerson_shouldReturnTrue() throws Exception {
        when(personService.deletePerson("Test", "Person")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Test")
                        .param("lastName", "Person"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
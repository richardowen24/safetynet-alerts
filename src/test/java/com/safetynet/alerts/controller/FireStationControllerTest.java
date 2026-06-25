package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireAddressDTO;
import com.safetynet.alerts.dto.FireResidentDTO;
import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FireStationService fireStationService;

    @Test
    public void getPersonsByStation_shouldReturn200AndCorrectData() throws Exception {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setPhone("841-874-6512");

        when(fireStationService.getPersonsByStation("3")).thenReturn(List.of(person));
        when(fireStationService.countAdultsByStation("3")).thenReturn(1L);
        when(fireStationService.countChildrenByStation("3")).thenReturn(0L);

        mockMvc.perform(get("/firestation").param("stationNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(1))
                .andExpect(jsonPath("$.childCount").value(0))
                .andExpect(jsonPath("$.persons[0].firstName").value("John"));
    }

    @Test
    public void getPhoneAlert_shouldReturn200AndPhoneList() throws Exception {
        when(fireStationService.getPhoneNumbersByStation("3"))
                .thenReturn(List.of("841-874-6512", "841-874-6513"));

        mockMvc.perform(get("/phoneAlert").param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("841-874-6512"))
                .andExpect(jsonPath("$[1]").value("841-874-6513"));
    }

    @Test
    public void getFireInfo_shouldReturn200AndCorrectData() throws Exception {
        FireResidentDTO resident = new FireResidentDTO("John", "Boyd", "841-874-6512", 42, List.of(), List.of());
        FireAddressDTO dto = new FireAddressDTO("3", List.of(resident));

        when(fireStationService.getResidentsByAddress("1509 Culver St")).thenReturn(dto);

        mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"));
    }

    @Test
    public void getFloodStations_shouldReturn200AndCorrectData() throws Exception {
        FireResidentDTO resident = new FireResidentDTO("John", "Boyd", "841-874-6512", 42, List.of(), List.of());
        FloodAddressDTO dto = new FloodAddressDTO("1509 Culver St", List.of(resident));

        when(fireStationService.getHouseholdsByStations(List.of("3"))).thenReturn(List.of(dto));

        mockMvc.perform(get("/flood/stations").param("stations", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"));
    }

    @Test
    public void addFireStation_shouldReturn200AndCreatedMapping() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("999 Test Ave");
        fireStation.setStation("9");

        when(fireStationService.addFireStation(org.mockito.ArgumentMatchers.any(FireStation.class)))
                .thenReturn(fireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"999 Test Ave\",\"station\":\"9\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("999 Test Ave"));
    }

    @Test
    public void updateFireStation_shouldReturnTrue() throws Exception {
        when(fireStationService.updateFireStation("999 Test Ave", "10")).thenReturn(true);

        mockMvc.perform(put("/firestation")
                        .param("address", "999 Test Ave")
                        .param("station", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void deleteFireStation_shouldReturnTrue() throws Exception {
        when(fireStationService.deleteFireStationByAddress("999 Test Ave")).thenReturn(true);

        mockMvc.perform(delete("/firestation").param("address", "999 Test Ave"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
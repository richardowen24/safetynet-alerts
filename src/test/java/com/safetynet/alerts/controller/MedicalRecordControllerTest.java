package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Test
    public void addMedicalRecord_shouldReturn200AndCreatedRecord() throws Exception {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("Test");
        record.setLastName("Patient");
        record.setBirthdate("01/01/1990");

        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(record);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"Patient\",\"birthdate\":\"01/01/1990\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    public void updateMedicalRecord_shouldReturnTrue() throws Exception {
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"Patient\",\"birthdate\":\"01/01/1985\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void deleteMedicalRecord_shouldReturnTrue() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("Test", "Patient")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Test")
                        .param("lastName", "Patient"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}

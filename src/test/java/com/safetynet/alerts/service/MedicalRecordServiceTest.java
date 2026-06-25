package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MedicalRecordServiceTest {

    private DataRepository mockRepository;
    private MedicalRecordService medicalRecordService;
    private MedicalRecord record;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(DataRepository.class);
        medicalRecordService = new MedicalRecordService(mockRepository);

        record = new MedicalRecord();
        record.setFirstName("Test");
        record.setLastName("Patient");
        record.setBirthdate("01/01/1990");
    }

    @Test
public void addMedicalRecord_shouldReturnSavedRecord() {
    MedicalRecord result = medicalRecordService.addMedicalRecord(record);

    assertEquals("Test", result.getFirstName());
}

    @Test
    public void updateMedicalRecord_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.updateMedicalRecord(record)).thenReturn(true);

        boolean result = medicalRecordService.updateMedicalRecord(record);

        assertTrue(result);
    }

    @Test
    public void deleteMedicalRecord_shouldReturnTrue_whenRepositoryReturnsTrue() {
        when(mockRepository.deleteMedicalRecord("Test", "Patient")).thenReturn(true);

        boolean result = medicalRecordService.deleteMedicalRecord("Test", "Patient");

        assertTrue(result);
    }
}

package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/medicalRecord")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.debug("Adding medical record: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            MedicalRecord result = medicalRecordService.addMedicalRecord(medicalRecord);
            logger.info("Added medical record for {} {}", result.getFirstName(), result.getLastName());
            return result;
        } catch (Exception e) {
            logger.error("Error adding medical record: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/medicalRecord")
    public boolean updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.debug("Updating medical record: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        try {
            boolean updated = medicalRecordService.updateMedicalRecord(medicalRecord);
            logger.info("Update medical record for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), updated);
            return updated;
        } catch (Exception e) {
            logger.error("Error updating medical record for {} {}: {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName(), e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/medicalRecord")
    public boolean deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        logger.debug("Deleting medical record: {} {}", firstName, lastName);
        try {
            boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
            logger.info("Delete medical record for {} {}: {}", firstName, lastName, deleted);
            return deleted;
        } catch (Exception e) {
            logger.error("Error deleting medical record for {} {}: {}", firstName, lastName, e.getMessage());
            throw e;
        }
    }
}
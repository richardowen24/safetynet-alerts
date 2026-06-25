package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    private final DataRepository dataRepository;

    public MedicalRecordService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        dataRepository.addMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        return dataRepository.updateMedicalRecord(medicalRecord);
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return dataRepository.deleteMedicalRecord(firstName, lastName);
    }
}

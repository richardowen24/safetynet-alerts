package com.safetynet.alerts.repository;

import tools.jackson.databind.json.JsonMapper;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepository {

    private List<Person> persons = new ArrayList<>();
    private List<FireStation> fireStations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @PostConstruct
    private void loadData() {
        try {
            JsonMapper jsonMapper = JsonMapper.builder().build();
            ClassPathResource resource = new ClassPathResource("data.json");
            DataContainer container = jsonMapper.readValue(resource.getInputStream(), DataContainer.class);

            this.persons = container.getPersons();
            this.fireStations = container.getFirestations();
            this.medicalRecords = container.getMedicalrecords();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data.json", e);
        }
    }

    public List<Person> getPersons() {
        return new ArrayList<>(persons);
    }

    public List<FireStation> getFireStations() {
        return new ArrayList<>(fireStations);
    }

    public List<MedicalRecord> getMedicalRecords() {
        return new ArrayList<>(medicalRecords);
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public boolean updatePerson(Person updatedPerson) {
        for (int i = 0; i < persons.size(); i++) {
            Person existing = persons.get(i);
            if (existing.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && existing.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
                persons.set(i, updatedPerson);
                return true;
            }
        }
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {
        return persons.removeIf(person ->
                person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName));
    }

    public void addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
    }

    public boolean updateFireStation(String address, String newStationNumber) {
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equalsIgnoreCase(address)) {
                fireStation.setStation(newStationNumber);
                return true;
            }
        }
        return false;
    }

    public boolean deleteFireStationByAddress(String address) {
        return fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address));
    }
}
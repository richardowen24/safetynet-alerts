package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireStationCoverageDTO {
    private List<PersonSummaryDTO> persons;
    private long adultCount;
    private long childCount;
}

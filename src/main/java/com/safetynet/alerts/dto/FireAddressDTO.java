package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireAddressDTO {
    private String stationNumber;
    private List<FireResidentDTO> residents;
}
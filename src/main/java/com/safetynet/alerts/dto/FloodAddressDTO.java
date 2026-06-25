package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloodAddressDTO {
    private String address;
    private List<FireResidentDTO> residents;
}

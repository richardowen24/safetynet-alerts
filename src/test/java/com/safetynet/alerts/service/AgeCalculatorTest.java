package com.safetynet.alerts.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgeCalculatorTest {

    @Test
    public void calculateAge_shouldReturnCorrectAge_forGivenBirthdate() {
        AgeCalculator ageCalculator = new AgeCalculator();
        int age = ageCalculator.calculateAge("03/06/1984");
        assertEquals(42, age);
    }
}
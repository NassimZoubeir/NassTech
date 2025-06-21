package com.ensup.nasstech.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrdinateurTest {

    @Test
    void testPrixOrdinateur() {
        Ordinateur ordi = new Ordinateur();
        ordi.setPrix(999.99);
        assertEquals(999.99, ordi.getPrix());
    }

    @Test
    void testDenominationOrdinateur() {
        Ordinateur ordi = new Ordinateur();
        ordi.setDenomination("Asus Vivobook");
        assertEquals("Asus Vivobook", ordi.getDenomination());
    }
}

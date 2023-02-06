package org.kq.addressbook;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BuddyInfoTest {

    @Test
    public void testGetName() {
        String name = "Bob";
        String phoneNumber = "555-222-1111";
        BuddyInfo bud = new BuddyInfo(name, phoneNumber);
        assertEquals(name, bud.getName());
    }

    @Test
    public void testSetName() {
        String name1 = "Bob";
        String name2 = "Mob";
        String phoneNumber = "555-222-1111";
        BuddyInfo bud = new BuddyInfo(name1, phoneNumber);
        bud.setName(name2);
        assertEquals(name2, bud.getName());
    }

    @Test
    public void testGetPhoneNumber() {
        String name = "Bob";
        String phoneNumber = "555-222-1111";
        BuddyInfo bud = new BuddyInfo(name, phoneNumber);
        assertEquals(phoneNumber, bud.getPhoneNumber());
    }

    @Test
    public void testSetPhoneNumber() {
        String name = "Bob";
        String phoneNumber1 = "555-222-1111";
        String phoneNumber2 = "555-222-3333";
        BuddyInfo bud = new BuddyInfo(name, phoneNumber1);
        bud.setPhoneNumber(phoneNumber2);
        assertEquals(phoneNumber2, bud.getPhoneNumber());
    }

    @Test
    public void testToString() {
        String name = "Bob";
        String phoneNumber = "555-222-1111";
        String asString = name + ": " + phoneNumber;
        BuddyInfo bud = new BuddyInfo(name, phoneNumber);
        assertEquals(asString, bud.toString());
    }
}
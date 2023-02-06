package org.kq.addressbook;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AddressBookTest {
    @Test
    public void testAddBuddy() {
        AddressBook ab = new AddressBook();
        BuddyInfo bud1 = new BuddyInfo("Bob", "555-222-1111");
        ab.addBuddy(bud1);
        List<BuddyInfo> budList = new ArrayList<>();
        budList.add(bud1);
        assertEquals(budList, ab.getBuddies());
    }

    @Test
    public void testRemoveBuddy() {
        AddressBook ab = new AddressBook();
        BuddyInfo bud1 = new BuddyInfo("Bob", "555-222-1111");
        BuddyInfo bud2 = new BuddyInfo("Mob", "555-222-4444");
        ab.addBuddy(bud1);
        ab.addBuddy(bud2);
        ab.removeBuddy(bud1);
        List<BuddyInfo> budList = new ArrayList<>();
        budList.add(bud2);
        assertEquals(budList, ab.getBuddies());
    }

    @Test
    public void testGetBuddies() {
        AddressBook ab = new AddressBook();
        BuddyInfo bud1 = new BuddyInfo("Bob", "555-222-1111");
        ab.addBuddy(bud1);
        List<BuddyInfo> budList = new ArrayList<>();
        budList.add(bud1);
        assertEquals(budList, ab.getBuddies());
    }
    @Test
    public void testToString() {
        AddressBook ab = new AddressBook();
        String name1 = "Bob";
        String phoneNumber1 = "555-222-1111";
        String asString1 = name1 + ": " + phoneNumber1;
        BuddyInfo bud1 = new BuddyInfo(name1, phoneNumber1);
        String name2 = "Mob";
        String phoneNumber2 = "555-222-4444";
        String asString2 = name2 + ": " + phoneNumber2;
        BuddyInfo bud2 = new BuddyInfo(name2, phoneNumber2);
        ab.addBuddy(bud1);
        ab.addBuddy(bud2);
        assertEquals(asString1 + "\n" + asString2, ab.toString());
    }
}
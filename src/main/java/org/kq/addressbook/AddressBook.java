package org.kq.addressbook;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<BuddyInfo> buddies;

    protected AddressBook() {
        buddies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void addBuddy(BuddyInfo buddy) {
        buddies.add(buddy);
    }

    public boolean removeBuddy(BuddyInfo buddy) {
        return buddies.remove(buddy);
    }

    public List<BuddyInfo> getBuddies() {
        return buddies;
    }
    public void setBuddies(List<BuddyInfo> buddies) {
        this.buddies = buddies;
    }

    @Override
    public String toString() {
        String ret = "";
        for (BuddyInfo bud : buddies) {
            ret += bud + "\n";
        }
        return ret.trim();
    }
    public static void main(String[] args) {
        BuddyInfo bud1 = new BuddyInfo("Bob", "555-000-1111");
        BuddyInfo bud2 = new BuddyInfo("Mob", "555-000-2222");
        BuddyInfo bud3 = new BuddyInfo("Lob", "555-000-3333");
        AddressBook ab = new AddressBook();

        ab.addBuddy(bud1);
        ab.addBuddy(bud2);
        ab.addBuddy(bud3);
        System.out.println(ab);
    }
}
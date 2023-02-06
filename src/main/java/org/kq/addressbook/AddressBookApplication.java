package org.kq.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AddressBookApplication {

    private static final Logger log = LoggerFactory.getLogger(AddressBookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AddressBookApplication.class, args);
    }

    //@Bean
    public CommandLineRunner demo1(BuddyInfoRepository repository) {
        return (args) -> {
            repository.save(new BuddyInfo("Bob", "555-222-1111"));
            repository.save(new BuddyInfo("Mob", "555-222-2222"));
            repository.save(new BuddyInfo("Lob", "555-222-3333"));

            log.info("Buddies found with findAll():");
            for (BuddyInfo bud : repository.findAll()) {
                log.info(bud.toString());
            }
            log.info("");

            BuddyInfo bud = repository.findById(1L);
            log.info("Buddy found with findById(1L):");
            log.info(bud.toString());
            log.info("");
        };
    }

    //@Bean
    public CommandLineRunner demo2(AddressBookRepository repository) {
        return (args) -> {
            List<BuddyInfo> buds = new ArrayList<>();
            buds.add(new BuddyInfo("Bob", "555-222-1111"));
            buds.add(new BuddyInfo("Mob", "555-222-2222"));
            buds.add(new BuddyInfo("Lob", "555-222-3333"));
            AddressBook a = new AddressBook();
            a.setBuddies(buds);
            repository.save(a);

            log.info("AddressBook found with findAll():");
            for (AddressBook ab : repository.findAll()) {
                log.info(ab.toString());
                log.info("Buddies found in the address book:");
                for (BuddyInfo bud : ab.getBuddies()) {
                    log.info(bud.toString());
                }
            }
            log.info("");

            AddressBook ab = repository.findById(1L);
            log.info("Address book found with findById(1L):");
            log.info(ab.toString());
            log.info("");
        };
    }

}

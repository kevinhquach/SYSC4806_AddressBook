package org.kq.addressbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final AddressBookRepository repository;
    HomeController(AddressBookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/home")
    public String home(@RequestParam Integer id, Model model) {
        AddressBook ab = repository.findById(id);
        if (ab != null) {
            List<BuddyInfo> buds = ab.getBuddies();
            model.addAttribute("budList", buds);
        }
        return "home";
    }
}

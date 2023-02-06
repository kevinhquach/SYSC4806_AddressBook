package org.kq.addressbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final AddressBookRepository repository; //should probably make a service layer instead
    HomeController(AddressBookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/home")
    public String home(@RequestParam Integer id, Model model) {
        //assume that only the first addressbook is to be returned, and that it exists
        AddressBook ab = repository.findById(id);
        if (ab != null) {
            List<BuddyInfo> buds = ab.getBuddies();
            model.addAttribute("budList", buds);
        }
        return "home";
    }
}

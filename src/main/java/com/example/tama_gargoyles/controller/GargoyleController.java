package com.example.tama_gargoyles.controller;

import com.example.tama_gargoyles.model.Gargoyle;
import com.example.tama_gargoyles.repository.GargoyleRepository;
import com.example.tama_gargoyles.repository.UserRepository;
import com.example.tama_gargoyles.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GargoyleController {

    @Autowired
    GargoyleRepository gargoyleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/gargoyles")
    public ModelAndView allGargoyles() {
        ModelAndView modelAndView = new ModelAndView("gargoyles/gargoyles");
        modelAndView.addObject("gargoyles", gargoyleRepository.findAll());
        return modelAndView;
}

    @GetMapping("/gargoyles/new")
    public ModelAndView newGargoyleForm() {
        // this is the space referred to in th:object (look back at the form code)
        Gargoyle gargoyle = new Gargoyle();
        ModelAndView newGargoyleForm = new ModelAndView("gargoyles/new");
        newGargoyleForm.addObject("gargoyle", gargoyle);
        return newGargoyleForm;
    }
    @PostMapping("/gargoyles")
    // Spring Boot uses the form data to create an instance of gargoyle
    // which is then passed in as an arg here
    public RedirectView create(Gargoyle gargoyle) {
        User user = userRepository.findById(1L).orElseThrow();
        gargoyle.setUser(user);
        gargoyleRepository.save(gargoyle);
        return new RedirectView("/gargoyles");
    }

}

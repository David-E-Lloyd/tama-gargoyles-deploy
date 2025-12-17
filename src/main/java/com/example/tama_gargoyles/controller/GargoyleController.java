package com.example.tama_gargoyles.controller;

import com.example.tama_gargoyles.model.Gargoyle;
import com.example.tama_gargoyles.model.User;
import com.example.tama_gargoyles.repository.GargoyleRepository;
import com.example.tama_gargoyles.service.CurrentUserService;
import com.example.tama_gargoyles.service.GargoyleTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;



@Controller
public class GargoyleController {

    private final GargoyleRepository gargoyleRepository;
    private final CurrentUserService currentUserService;
    private final GargoyleTimeService timeService;

    public GargoyleController(
            GargoyleRepository gargoyleRepository,
            CurrentUserService currentUserService,
            GargoyleTimeService timeService
    ) {
        this.gargoyleRepository = gargoyleRepository;
        this.currentUserService = currentUserService;
        this.timeService = timeService;
    }

    @PostMapping("/hunger")
    public RedirectView decreaseHunger(@RequestParam Integer delta, @RequestParam Long gargoyleId){
        Gargoyle gargoyle = gargoyleRepository.findById(gargoyleId).get();
        gargoyle.setHunger(Math.min(gargoyle.getHunger() + delta, 100));
        gargoyleRepository.save(gargoyle);
        return new RedirectView("/game");
    }

    @GetMapping("/game")
    public String game(Model model, Authentication authentication) {
        User user = currentUserService.getCurrentUser(authentication);

        var gargoyles = gargoyleRepository.findAllByUserIdOrderByIdAsc(user.getId());

        // If this is a brand new Auth0 user, create their first creature.
        if (gargoyles.isEmpty()) {
            Gargoyle newborn = new Gargoyle(user);
            newborn.setName("Egg-" + user.getId()); // UNIQUE constraint safe
            gargoyleRepository.save(newborn);
            gargoyles = java.util.List.of(newborn);
        }

        // MVP selection rule:
        // Prefer CHILD if one exists, otherwise pick the first by id.
        Gargoyle g = gargoyles.stream()
                .filter(x -> x.getType() == Gargoyle.Type.CHILD)
                        .findFirst()
                                .orElse(gargoyles.get(0));

        // ROCK-SOLID ORDER:
        // 1) Resume first (prevents offline gap)
        // 2) Then tick (applies only active time)
        timeService.resume(g);
        timeService.tick(g);

        gargoyleRepository.save(g);

        model.addAttribute("gargoyle", g);
        model.addAttribute("gameDaysOld", timeService.gameDaysOld(g));
        model.addAttribute("minutesIntoDay", timeService.minutesIntoCurrentDay(g));

        return "game";
    }

    @PostMapping("/gargoyles/pause")
    public String pause(Authentication authentication) {
        User user = currentUserService.getCurrentUser(authentication);

        var gargoyles = gargoyleRepository.findAllByUserIdOrderByIdAsc(user.getId());
        if (gargoyles.isEmpty()) return "redirect:/";

        Gargoyle g = gargoyles.stream()
                        .filter(x -> x.getType() == Gargoyle.Type.CHILD)
                                .findFirst()
                                        .orElse(gargoyles.get(0));

        timeService.pause(g);
        gargoyleRepository.save(g);

        return "redirect:/";
    }
}

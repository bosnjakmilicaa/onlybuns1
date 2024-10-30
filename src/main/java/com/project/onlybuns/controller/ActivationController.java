package com.project.onlybuns.controller;

import com.project.onlybuns.model.UnregisteredUser;
import com.project.onlybuns.service.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivationController {

    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public ActivationController(UnregisteredUserService unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("token") String token) {
        // Ovdje treba dodati logiku za aktivaciju naloga koristeći token
        // Na primer, pronaći korisnika i postaviti isActive na true
        return "Korisnik je aktiviran!"; // Ovo je samo primer, možeš dodati odgovarajući odgovor
    }
}

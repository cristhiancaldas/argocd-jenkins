package com.app.argocd.controller;

import com.app.argocd.service.ArgocdService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ArgoCdController {

    @Autowired
    private ArgocdService argocdService;
    @GetMapping
    public String messageDummy(){
        return "Dummy - AWS Console-ARGOCD";
    }

    @GetMapping("/persons")
    public JsonNode getRandomPersons() {
        return argocdService.persons();
    }

    @GetMapping("/foods")
    public JsonNode getRandomFoods() {
        return argocdService.foods();
    }

    @GetMapping("/books")
    public JsonNode getRandomBook() {
        return argocdService.books();
    }
}

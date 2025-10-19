package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DrinkEntryController {

    @GetMapping("/Drink")
    public String Drinking() { return "Hallo, hast du heute schon was getrunken?";
    }

}
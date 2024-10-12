package com.shopi.shopping.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home"; // This should return the name of your home HTML page (e.g., home.html)
    }
}

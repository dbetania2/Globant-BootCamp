package com.shopi.shopping.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    //------
    @Operation(summary = "Get home page", description = "Retrieves the home page of the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Home page retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    //------
    @GetMapping("/home")
    public String home() {
        return "home"; // This should return the name of your home HTML page (e.g., home.html)
    }
}

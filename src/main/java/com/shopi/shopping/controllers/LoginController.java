package com.shopi.shopping.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //------
    @Operation(summary = "Get login page", description = "Retrieves the login page for user authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login page retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    //------
    @GetMapping("/login")
    public String login() {
        return "login"; // Returns the name of the view (login.html)
    }
}
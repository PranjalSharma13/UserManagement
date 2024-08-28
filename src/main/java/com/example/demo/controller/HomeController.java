package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {
    @GetMapping
    public String homeMethod() {
       return "Test Home";
    }
}

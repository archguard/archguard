package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sub")
public class HelloController {

    @GetMapping("/overview")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
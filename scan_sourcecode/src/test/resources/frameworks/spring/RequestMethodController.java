package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestMethodController {

    @RequestMapping(value = "/api/request-method", method = {RequestMethod.GET})
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/api/request-method", method = { RequestMethod.POST })
    public String index() {
        return "Some Post response";
    }

}
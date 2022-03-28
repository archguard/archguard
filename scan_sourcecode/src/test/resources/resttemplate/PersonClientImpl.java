package com.journaldev.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journaldev.spring.model.Person;

@Service
public class PersonClientImpl implements PersonClient {

    @Autowired
    RestTemplate restTemplate;

    public List<Person> getAllPerson() {
        ResponseEntity<Person[]> response = restTemplate.getForEntity("/springData/person", Person[].class);
        return Arrays.asList(response.getBody());

    }
}
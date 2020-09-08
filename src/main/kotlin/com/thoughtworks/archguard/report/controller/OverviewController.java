package com.thoughtworks.archguard.report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/systems/{systemId}/overview")
public class OverviewController {
    @GetMapping
    public void overview() {

    }
}

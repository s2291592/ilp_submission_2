package com.ilp.ilp_submission_2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UuidGetController {
    /**
     * Return a string
     * @return student uuid
     */
    @GetMapping("/uuid")
    public String getUuid() {
        return "s2291592";
    }
}
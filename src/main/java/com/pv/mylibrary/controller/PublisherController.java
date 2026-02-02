package com.pv.mylibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController {

    @GetMapping
    public ResponseEntity<String> getAllPublishers() {
        return ResponseEntity.ok("All Publishers");
    }

}

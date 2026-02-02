package com.pv.mylibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    @GetMapping
    public ResponseEntity<String> getAllAuthors() {
        return ResponseEntity.ok("All Authors");
    }

}

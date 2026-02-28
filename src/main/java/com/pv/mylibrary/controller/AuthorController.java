package com.pv.mylibrary.controller;

import com.pv.mylibrary.dto.AuthorDto;
import com.pv.mylibrary.service.AuthorService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PostMapping
    public ResponseEntity<AuthorDto> insertNewAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.insertNewAuthor(authorDto));
    }

    @PutMapping
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto authorDtoUpdated = authorService.updateAuthor(authorDto);
        return ResponseEntity.ok(authorDtoUpdated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable(value = "id") Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

}

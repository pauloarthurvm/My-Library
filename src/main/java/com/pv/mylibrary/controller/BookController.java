package com.pv.mylibrary.controller;

import com.pv.mylibrary.dto.BookDto;
import com.pv.mylibrary.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> bookDtoList = bookService.getAllBooks();
        return ResponseEntity.ok(bookDtoList);
    }

    @PostMapping
    public ResponseEntity<BookDto> insertBookSave(@RequestBody BookDto bookDto) {
        BookDto bookDtoSaved = bookService.insertNewBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDtoSaved);
    }

    @PutMapping()
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
        Optional<BookDto> bookDtoUpdated = bookService.updateBook(bookDto);
        if(bookDtoUpdated.isPresent()) {
            return ResponseEntity.ok(bookDtoUpdated.get());
        }
        return ResponseEntity.notFound().build();
    }

}

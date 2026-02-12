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

    private final BookService bookService;

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
        BookDto bookDtoUpdated = bookService.updateBook(bookDto);
        return ResponseEntity.ok(bookDtoUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

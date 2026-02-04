package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.BookDto;
import com.pv.mylibrary.entity.BookEntity;
import com.pv.mylibrary.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntity -> toDto(bookEntity))
                .collect(Collectors.toList());
    }

    public BookDto insertNewBook(BookDto bookDto) {
        BookEntity entity = new BookEntity();
        entity.setTitle(bookDto.title());
        BookEntity saved = bookRepository.save(entity);
        return toDto(saved);
    }

    private BookDto toDto(BookEntity bookEntity) {
        return new BookDto(bookEntity.getId(), bookEntity.getTitle());
    }

}

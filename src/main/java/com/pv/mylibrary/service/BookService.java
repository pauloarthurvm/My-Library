package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.BookDto;
import com.pv.mylibrary.entity.BookEntity;
import com.pv.mylibrary.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntity -> toDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BookDto> updateBook(BookDto bookDto) {
        Optional<BookEntity> bookEntityOpt = bookRepository.findById(bookDto.id());
        if(bookEntityOpt.isPresent()) {
            BookEntity bookEntity = bookEntityOpt.get();
            bookEntity.setTitle(bookDto.title());
            return Optional.of(toDto(bookEntity));
        }
        return Optional.empty();
    }

    @Transactional
    public BookDto insertNewBook(BookDto bookDto) {
        BookEntity entity = new BookEntity();
        entity.setTitle(bookDto.title());
        BookEntity saved = bookRepository.save(entity);
        return toDto(saved);
    }

    private BookDto toDto(BookEntity bookEntity) {
        return new BookDto(
                bookEntity.getId(),
                bookEntity.getTitle());
    }

}

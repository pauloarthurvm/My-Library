package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.AuthorSummaryDto;
import com.pv.mylibrary.dto.BookDto;
import com.pv.mylibrary.dto.BookSummaryDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.entity.BookEntity;
import com.pv.mylibrary.entity.PublisherEntity;
import com.pv.mylibrary.repository.AuthorRepository;
import com.pv.mylibrary.repository.BookRepository;
import com.pv.mylibrary.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       PublisherRepository publisherRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntity -> toDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BookDto> insertNewBook(BookDto bookDto) {
        BookEntity entity = new BookEntity();

        List<Optional<AuthorEntity>> authorsOpt = bookDto.authors().stream()
                .map(a -> authorRepository.findById(a.id()))
                .collect(Collectors.toList());
        for (Optional<AuthorEntity> a : authorsOpt) {
            if (a.isEmpty()) {
                return Optional.empty();
            }
        }

        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(bookDto.publisherId());
        if(publisherEntityOpt.isPresent()) {
            entity.setTitle(bookDto.title());
            entity.setPublisherEntity(publisherEntityOpt.get());
            entity.setAuthors(authorsOpt.stream()
                    .map(Optional::get).collect(Collectors.toSet()));
            BookEntity saved = bookRepository.save(entity);
            return Optional.of(toDto(saved));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<BookDto> updateBook(BookDto bookDto) {
        Optional<BookEntity> bookEntityOpt = bookRepository.findById(bookDto.id());
        if(bookEntityOpt.isEmpty()) {
            return Optional.empty();
        }
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(bookDto.publisherId());
        if(publisherEntityOpt.isEmpty()) {
            return Optional.empty();
        }
        List<Optional<AuthorEntity>> authorsOpt = bookDto.authors().stream()
                .map(a -> authorRepository.findById(a.id()))
                .collect(Collectors.toList());
        for (Optional<AuthorEntity> a : authorsOpt) {
            if (a.isEmpty()) {
                return Optional.empty();
            }
        }

        BookEntity bookEntity = bookEntityOpt.get();
        bookEntity.setTitle(bookDto.title());
        bookEntity.setPublisherEntity(publisherEntityOpt.get());
        bookEntity.setAuthors(authorsOpt.stream()
                .map(Optional::get).collect(Collectors.toSet()));
        return Optional.of(toDto(bookEntity));
    }

    private BookDto toDto(BookEntity bookEntity) {
        Set<AuthorSummaryDto> authors = new HashSet<>();
        bookEntity.getAuthors().stream().forEach(a -> {
            authors.add(new AuthorSummaryDto(a.getId(), a.getFullname()));
        });
        return new BookDto(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getPublisherEntity().getId(),
                bookEntity.getPublisherEntity().getName(),
                authors);
    }

}

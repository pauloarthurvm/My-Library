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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BookService(BookRepository bookRepository,
                       PublisherRepository publisherRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        logger.info("Get all books");
        return bookRepository.findAll().stream()
                .map(bookEntity -> toDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BookDto> insertNewBook(BookDto bookDto) {
        logger.info("Insert new book: {}", bookDto);
        BookEntity entity = new BookEntity();

        List<Optional<AuthorEntity>> authorsOpt = bookDto.authors().stream()
                .map(a -> authorRepository.findById(a.id()))
                .collect(Collectors.toList());
        for (Optional<AuthorEntity> a : authorsOpt) {
            if (a.isEmpty()) {
                logger.error("Could not find an author.");
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
            logger.info("Book inserted successfully.");
            return Optional.of(toDto(saved));
        }
        logger.error("Could not find publisher.");
        return Optional.empty();
    }

    @Transactional
    public Optional<BookDto> updateBook(BookDto bookDto) {
        logger.info("Update a book: {}", bookDto);
        Optional<BookEntity> bookEntityOpt = bookRepository.findById(bookDto.id());
        if(bookEntityOpt.isEmpty()) {
            logger.error("Could not find book by id.");
            return Optional.empty();
        }
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(bookDto.publisherId());
        if(publisherEntityOpt.isEmpty()) {
            logger.error("Could not find publisher.");
            return Optional.empty();
        }
        List<Optional<AuthorEntity>> authorsOpt = bookDto.authors().stream()
                .map(a -> authorRepository.findById(a.id()))
                .collect(Collectors.toList());
        for (Optional<AuthorEntity> a : authorsOpt) {
            if (a.isEmpty()) {
                logger.error("Could not find an author.");
                return Optional.empty();
            }
        }
        BookEntity bookEntity = bookEntityOpt.get();
        bookEntity.setTitle(bookDto.title());
        bookEntity.setPublisherEntity(publisherEntityOpt.get());
        bookEntity.setAuthors(authorsOpt.stream()
                .map(Optional::get).collect(Collectors.toSet()));
        logger.info("Book updated successfully.");
        return Optional.of(toDto(bookEntity));
    }

    @Transactional
    public boolean deleteBookById(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
            logger.error("Could not find book by id.");
            return false;
        }
        bookRepository.deleteById(bookId);
        logger.info("Book deleted successfully.");
        return true;
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

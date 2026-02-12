package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.AuthorDto;
import com.pv.mylibrary.dto.BookSummaryDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.entity.BookEntity;
import com.pv.mylibrary.exception.ConflictException;
import com.pv.mylibrary.exception.ResourceNotFoundException;
import com.pv.mylibrary.repository.AuthorRepository;
import com.pv.mylibrary.repository.BookRepository;
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
public class AuthorService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> getAllAuthors() {
        logger.info("Get all authors");
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AuthorDto insertNewAuthor(AuthorDto authorDto) {
        logger.info("Insert new author: {}", authorDto);
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setFullname(authorDto.fullname());
        authorEntity = authorRepository.save(authorEntity);
        return toDto(authorEntity);
    }

    @Transactional
    public AuthorDto updateAuthor(AuthorDto authorDto) {
        logger.info("Update an author - ID: {}", authorDto.id());
        Optional<AuthorEntity> authorEntityOpt = authorRepository.findById(authorDto.id());
        if(authorEntityOpt.isEmpty()) {
            logger.warn("Author not found - ID:{}", authorDto.id());
            throw new ResourceNotFoundException(String.format("Author not found - ID: %d", authorDto.id()));
        }
        AuthorEntity authorEntity = authorEntityOpt.get();
        authorEntity.setFullname(authorDto.fullname());
        return toDto(authorEntity);
    }

    @Transactional
    public void deleteAuthor(Long authorId) {
        Optional<AuthorEntity> authorEntityOpt = authorRepository.findById(authorId);
        if (authorEntityOpt.isEmpty()) {
            logger.warn("Author not found - ID: {}", authorId);
            throw new ResourceNotFoundException(String.format("Author not found - ID:%d", authorId));
        }
//        if (authorEntityOpt.get().getBooks().size() > 0) {
        if(authorRepository.hasAnyBook(authorId)) {
            logger.warn("Can not delete author ID {} - Author has books linked.", authorId);
            throw new ConflictException(String.format("Can not delete author ID: %d - Author has books linked.", authorId));
        }
        authorRepository.deleteById(authorId);
        return;
    }

    private AuthorDto toDto(AuthorEntity authorEntity) {
        Set<BookSummaryDto> books = new HashSet<>();
        authorEntity.getBooks().stream().forEach(b -> {
            books.add(new BookSummaryDto(b.getId(), b.getTitle()));
        });
        return new AuthorDto(
                authorEntity.getId(),
                authorEntity.getFullname(),
                books);
    }

}

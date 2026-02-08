package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.AuthorDto;
import com.pv.mylibrary.dto.BookSummaryDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.entity.BookEntity;
import com.pv.mylibrary.repository.AuthorRepository;
import com.pv.mylibrary.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AuthorDto insertNewAuthor(AuthorDto authorDto) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setFullname(authorDto.fullname());
        authorEntity = authorRepository.save(authorEntity);
        return toDto(authorEntity);
    }

    @Transactional
    public Optional<AuthorDto> updateAuthor(AuthorDto authorDto) {
        Optional<AuthorEntity> authorEntityOpt = authorRepository.findById(authorDto.id());
        if(authorEntityOpt.isPresent()) {
            AuthorEntity authorEntity = authorEntityOpt.get();
            authorEntity.setFullname(authorDto.fullname());
            return Optional.of(toDto(authorEntity));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteAuthor(Long authorId) {
        Optional<AuthorEntity> authorEntityOpt = authorRepository.findById(authorId);
        if (authorEntityOpt.isEmpty()) {
            System.out.println("Author does not exist");
            return false;
        }
//        if (authorEntityOpt.get().getBooks().size() > 0) {
        if(authorRepository.hasAnyBook(authorId)) {
            System.out.println("Can not erase the author - Author has books linked.");
            return false;
        }
        authorRepository.deleteById(authorId);
        return true;
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

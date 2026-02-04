package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.AuthorDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AuthorDto insertNewAuthor(AuthorDto authorDto) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setFullname(authorDto.fullname());
        authorEntity = authorRepository.save(authorEntity);
        return toDto(authorEntity);
    }

    private AuthorDto toDto(AuthorEntity authorEntity) {
        return new AuthorDto(authorEntity.getId(), authorEntity.getFullname());
    }
}

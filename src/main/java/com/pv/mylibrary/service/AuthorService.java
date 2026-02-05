package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.AuthorDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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

    private AuthorDto toDto(AuthorEntity authorEntity) {
        return new AuthorDto(authorEntity.getId(), authorEntity.getFullname());
    }
}

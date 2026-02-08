package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.PublisherDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.entity.PublisherEntity;
import com.pv.mylibrary.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional(readOnly = true)
    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisherEntity -> toDto(publisherEntity)).collect(Collectors.toList());
    }

    @Transactional
    public PublisherDto insertNewPublisher(PublisherDto publisherDto) {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName(publisherDto.name());
        publisherEntity = publisherRepository.save(publisherEntity);
        return toDto(publisherEntity);
    }

    @Transactional
    public Optional<PublisherDto> updatePublisher(PublisherDto publisherDto) {
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(publisherDto.id());
        if(publisherEntityOpt.isPresent()) {
            PublisherEntity publisherEntity = publisherEntityOpt.get();
            publisherEntity.setName(publisherDto.name());
            return Optional.of(toDto(publisherEntity));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deletePublisherById(Long publisherId) {
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(publisherId);
        if (publisherEntityOpt.isEmpty()) {
            System.out.println("Publisher does not exist");
            return false;
        }
        if(publisherRepository.hasAnyBook(publisherId)) {
            System.out.println("Can not erase the publisher - Publisher has books linked.");
            return false;
        }
        publisherRepository.deleteById(publisherId);
        return true;
    }

    private PublisherDto toDto(PublisherEntity publisherEntity) {
        return new PublisherDto(publisherEntity.getId(), publisherEntity.getName());
    }

}

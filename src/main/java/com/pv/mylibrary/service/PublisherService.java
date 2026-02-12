package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.PublisherDto;
import com.pv.mylibrary.entity.AuthorEntity;
import com.pv.mylibrary.entity.PublisherEntity;
import com.pv.mylibrary.exception.ConflictException;
import com.pv.mylibrary.exception.ResourceNotFoundException;
import com.pv.mylibrary.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional(readOnly = true)
    public List<PublisherDto> getAllPublishers() {
        logger.info("Get all publishers");
        return publisherRepository.findAll().stream()
                .map(publisherEntity -> toDto(publisherEntity)).collect(Collectors.toList());
    }

    @Transactional
    public PublisherDto insertNewPublisher(PublisherDto publisherDto) {
        logger.info("Insert new publisher: {}", publisherDto);
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName(publisherDto.name());
        publisherEntity = publisherRepository.save(publisherEntity);
        return toDto(publisherEntity);
    }

    @Transactional
    public Optional<PublisherDto> updatePublisher(PublisherDto publisherDto) {
        logger.info("Update a publisher - ID: {}", publisherDto.id());
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(publisherDto.id());
        if(publisherEntityOpt.isPresent()) {
            PublisherEntity publisherEntity = publisherEntityOpt.get();
            publisherEntity.setName(publisherDto.name());
            return Optional.of(toDto(publisherEntity));
        }
        logger.error("Could not find publisher");
        return Optional.empty();
    }

    @Transactional
    public void deletePublisherById(Long publisherId) {
        logger.info("Delete a publisher - ID: {}", publisherId);
        Optional<PublisherEntity> publisherEntityOpt = publisherRepository.findById(publisherId);
        if (publisherEntityOpt.isEmpty()) {
            logger.warn("Could not find publisher with ID - {}", publisherId);
            throw new ResourceNotFoundException("Publisher not found.");
        }
        if(publisherRepository.hasAnyBook(publisherId)) {
            logger.warn("Can not erase the publisher ID: - Publisher has books linked.", publisherId);
            throw new ConflictException("Publisher has books linked");
        }
        publisherRepository.deleteById(publisherId);
        return;
    }

    private PublisherDto toDto(PublisherEntity publisherEntity) {
        return new PublisherDto(publisherEntity.getId(), publisherEntity.getName());
    }

}

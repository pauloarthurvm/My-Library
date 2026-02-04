package com.pv.mylibrary.service;

import com.pv.mylibrary.dto.PublisherDto;
import com.pv.mylibrary.entity.PublisherEntity;
import com.pv.mylibrary.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisherEntity -> toDto(publisherEntity)).collect(Collectors.toList());
    }

    public PublisherDto insertNewPublisher(PublisherDto publisherDto) {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName(publisherDto.name());
        publisherEntity = publisherRepository.save(publisherEntity);
        return toDto(publisherEntity);
    }

    private PublisherDto toDto(PublisherEntity publisherEntity) {
        return new PublisherDto(publisherEntity.getId(), publisherEntity.getName());
    }

}

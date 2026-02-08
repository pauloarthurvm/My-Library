package com.pv.mylibrary.controller;

import com.pv.mylibrary.dto.PublisherDto;
import com.pv.mylibrary.entity.PublisherEntity;
import com.pv.mylibrary.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @PostMapping
    public ResponseEntity<PublisherDto> insertNewPublisher(@RequestBody PublisherDto publisherDto) {
        PublisherDto savedPublisherDto = publisherService.insertNewPublisher(publisherDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPublisherDto);
    }

    @PutMapping
    public ResponseEntity<PublisherDto> updatePublisher(@RequestBody PublisherDto publisherDto) {
        Optional<PublisherDto> publisherDtoUpdated = publisherService.updatePublisher(publisherDto);
        if(publisherDtoUpdated.isPresent()) {
            return ResponseEntity.ok(publisherDtoUpdated.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable(value = "id") Long publisherId) {

        boolean deleted = publisherService.deletePublisherById(publisherId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.notFound().build();
    }

}

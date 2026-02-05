package com.pv.mylibrary.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@NoArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherEntity publisherEntity;

//    @Column(name = "publisher_id", nullable = false)
//    private Long publisherId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PublisherEntity getPublisherEntity() {
        return publisherEntity;
    }

    public void setPublisherEntity(PublisherEntity publisherEntity) {
        this.publisherEntity = publisherEntity;
    }

}

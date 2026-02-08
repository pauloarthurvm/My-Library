package com.pv.mylibrary.repository;

import com.pv.mylibrary.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {


    @Query(value = """
        SELECT EXISTS (SELECT 1 FROM books where publisher_id = :id);
    """, nativeQuery = true)
    boolean hasAnyBook(@Param("id") Long publisherId);

}

package com.pv.mylibrary.repository;

import com.pv.mylibrary.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    @Query(value = """
        SELECT EXISTS (SELECT 1 FROM books_authors where author_id = :id);
    """, nativeQuery = true)
    boolean hasAnyBook(@Param("id") Long authorId);
    
}

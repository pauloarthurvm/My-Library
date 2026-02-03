package com.pv.mylibrary.repository;

import com.pv.mylibrary.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {


}

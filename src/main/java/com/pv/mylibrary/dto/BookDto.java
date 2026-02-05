package com.pv.mylibrary.dto;

public record BookDto(
        Long id,
        String title,
        Long publisherId,
        String publisherName
) {
}

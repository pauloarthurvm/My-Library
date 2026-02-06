package com.pv.mylibrary.dto;

import java.util.Set;

public record BookDto(
        Long id,
        String title,
        Long publisherId,
        String publisherName,
        Set<AuthorSummaryDto> authors) {
}

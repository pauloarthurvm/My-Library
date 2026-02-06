package com.pv.mylibrary.dto;

import java.util.Set;

public record AuthorDto(
        Long id,
        String fullname,
        Set<BookSummaryDto> books) {
}

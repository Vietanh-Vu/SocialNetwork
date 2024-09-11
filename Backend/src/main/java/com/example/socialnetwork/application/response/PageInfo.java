package com.example.socialnetwork.application.response;

import lombok.Builder;

@Builder
public record PageInfo (
    Integer page,
    Integer pageSize,
    Long totalElements,
    Integer totalPages,
    Boolean hasPrev,
    Boolean hasNext
) {
}

package com.mohammadaskar.sitepulse_api.client.api;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        Long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}

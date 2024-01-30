package edu.jcourse.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageResponse<T>(List<T> content,
                              MetaData metaData) {

    public static <T> PageResponse<T> of(Page<T> page) {
        MetaData metaData = new MetaData(page.getNumber(), page.getSize(), page.getTotalElements());
        return new PageResponse<>(page.getContent(), metaData);
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record MetaData(int page,
                           int size,
                           long totalElements) {
    }
}
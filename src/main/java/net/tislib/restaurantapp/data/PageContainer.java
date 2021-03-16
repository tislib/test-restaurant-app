package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "page container")
@FieldNameConstants
public class PageContainer<T> extends Resource<PageContainer<T>> {

    @Schema(description = "list of resources")
    private List<T> content;

    @Schema(description = "total elements count")
    private long totalElements;

    @Schema(description = "total pages count")
    private long totalPages;

    public <V> PageContainer<V> map(Function<T, V> mapper) {
        return PageContainer.<V>builder()
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(content.stream()
                        .map(mapper)
                        .collect(Collectors.toList()))
                .build();
    }
}

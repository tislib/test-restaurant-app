package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "page container")
public class PageContainer<T> extends Resource<PageContainer<T>> {

    @Schema(description = "list of resources")
    private List<T> content;

    @Schema(description = "total elements count")
    private long totalElements;

    @Schema(description = "total pages count")
    private long totalPages;
}

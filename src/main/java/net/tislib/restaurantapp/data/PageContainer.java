package net.tislib.restaurantapp.data;

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
public class PageContainer<T> extends Resource<PageContainer<T>> {

    private List<T> content;

    private long totalElements;

    private long totalPages;
}

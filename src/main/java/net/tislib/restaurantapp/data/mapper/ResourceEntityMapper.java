package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.PageContainer;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

public interface ResourceEntityMapper<R, E> {

    E from(R resource);

    R to(E entity);

    void mapFrom(@MappingTarget E entity, R resource);

    void mapTo(@MappingTarget R resource, E entity);

    default PageContainer<R> mapPage(Page<E> page) {
        Page<R> resourcePage = page.map(this::to);

        return PageContainer.<R>builder()
                .content(resourcePage.getContent())
                .totalElements(resourcePage.getTotalElements())
                .totalPages(resourcePage.getTotalPages())
                .build();
    }

}

package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ReviewService {
    ReviewResource create(Long restaurantId, ReviewResource resource);

    PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating, Pageable pageable);

    ReviewResource get(Long restaurantId, Long id);

    ReviewResource update(Long restaurantId, Long id, ReviewResource resource);

    OwnerReplyResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource ownerReplyResource);

    void delete(Long restaurantId, Long id);
}

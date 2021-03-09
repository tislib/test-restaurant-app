package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;

import java.math.BigDecimal;

public interface ReviewService {
    ReviewResource create(Long restaurantId, ReviewResource resource);

    PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating);

    ReviewResource getClass(Long restaurantId, Long id);

    ReviewResource update(Long restaurantId, Long id, ReviewResource resource);

    ReviewResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource ownerReplyResource);

    ReviewResource delete(Long restaurantId, Long id);
}

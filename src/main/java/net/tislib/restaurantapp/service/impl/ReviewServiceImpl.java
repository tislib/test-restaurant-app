package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    @Override
    public ReviewResource create(Long restaurantId, ReviewResource resource) {
        return null;
    }

    @Override
    public PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating, Pageable pageable) {
        return null;
    }

    @Override
    public ReviewResource getClass(Long restaurantId, Long id) {
        return null;
    }

    @Override
    public ReviewResource update(Long restaurantId, Long id, ReviewResource resource) {
        return null;
    }

    @Override
    public ReviewResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource ownerReplyResource) {
        return null;
    }

    @Override
    public ReviewResource delete(Long restaurantId, Long id) {
        return null;
    }
}

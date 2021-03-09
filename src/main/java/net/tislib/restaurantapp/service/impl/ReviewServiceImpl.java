package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.data.mapper.OwnerReplyMapper;
import net.tislib.restaurantapp.data.mapper.ReviewMapper;
import net.tislib.restaurantapp.model.OwnerReply;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.ReviewRepository;
import net.tislib.restaurantapp.service.RestaurantReviewStatsService;
import net.tislib.restaurantapp.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper mapper;
    private final OwnerReplyMapper ownerReplyMapper;
    private final RestaurantReviewStatsService reviewStatsService;

    @Override
    public ReviewResource create(Long restaurantId, ReviewResource resource) {
        Restaurant restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + restaurantId));

        Review entity = mapper.from(resource);

        entity.setId(null);
        entity.setReviewTime(Instant.now());
        entity.setRestaurant(restaurantEntity);
        repository.save(entity);

        reviewStatsService.computeReview((short) 0, entity, false);

        return get(restaurantId, entity.getId());
    }

    @Override
    public PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating, Pageable pageable) {
        return mapper.mapPage(repository.findAll(pageable));
    }

    @Override
    public ReviewResource get(Long restaurantId, Long id) {
        Review entity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("review not found with id: " + id));

        return mapper.to(entity);
    }

    @Override
    public ReviewResource update(Long restaurantId, Long id, ReviewResource resource) {
        Restaurant restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + restaurantId));

        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        short previousStarCount = existingEntity.getStarCount();

        resource.setId(null);
        mapper.mapTo(resource, existingEntity);

        existingEntity.setReviewTime(Instant.now());
        existingEntity.setRestaurant(restaurantEntity);

        repository.save(existingEntity);

        reviewStatsService.computeReview(previousStarCount, existingEntity, false);

        return get(restaurantId, resource.getId());
    }

    @Override
    public OwnerReplyResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource resource) {
        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        // locate owner reply
        OwnerReply ownerReply = existingEntity.getOwnerReply();
        if (ownerReply == null) {
            ownerReply = new OwnerReply();
        }

        ownerReplyMapper.mapTo(resource, existingEntity.getOwnerReply());

        // back reference
        ownerReply.setReview(existingEntity);
        existingEntity.setOwnerReply(ownerReply);

        repository.save(existingEntity);

        return ownerReplyMapper.to(ownerReply);
    }

    @Override
    public void delete(Long restaurantId, Long id) {
        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("review not found with id: " + id));

        repository.delete(existingEntity);

        short previousStarCount = existingEntity.getStarCount();
        existingEntity.setStarCount((short) 0);

        reviewStatsService.computeReview(previousStarCount, existingEntity, true);
    }
}

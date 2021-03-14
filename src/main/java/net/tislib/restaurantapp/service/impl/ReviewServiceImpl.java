package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.controller.RestaurantController;
import net.tislib.restaurantapp.controller.ReviewController;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.data.mapper.OwnerReplyMapper;
import net.tislib.restaurantapp.data.mapper.ReviewMapper;
import net.tislib.restaurantapp.model.OwnerReply;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.UserRole;
import net.tislib.restaurantapp.model.repository.OwnerReplyRepository;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.ReviewRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import net.tislib.restaurantapp.service.RestaurantReviewStatsService;
import net.tislib.restaurantapp.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    public static final String RESTAURANT = "restaurant";
    public static final String REVIEW = "review";
    private final ReviewRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper mapper;
    private final OwnerReplyMapper ownerReplyMapper;
    private final RestaurantReviewStatsService reviewStatsService;
    private final OwnerReplyRepository ownerReplyRepository;
    private final AuthenticationService authenticationService;

    @Override
    public ReviewResource create(Long restaurantId, ReviewResource resource) {
        log.trace("create review restaurantId: {}, request: {}", restaurantId, resource);

        Restaurant restaurantEntity = getRestaurantEntity(restaurantId);
        log.debug("restaurant found: {}", restaurantEntity);

        Review entity = mapper.from(resource);

        // revert entity some fields which is not expected to be set by caller
        entity.setId(null);
        entity.setReviewTime(Instant.now());
        entity.setRestaurant(restaurantEntity);
        entity.setUser(authenticationService.getCurrentUser()); // set user to current user

        log.debug("saving review to database: {}", entity);
        repository.save(entity);

        // compute restaurant review stats (average review, etc.)
        reviewStatsService.computeReview((short) 0, entity, 1);
        log.debug("review ({}) computed for restaurant: {}", entity.getId(), restaurantEntity.getId());

        return get(restaurantId, entity.getId());
    }

    @Override
    public PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating, Pageable pageable) {
        log.trace("list review request: {}, {}, {}", restaurantId, rating, pageable);

        return mapper.mapPage(repository.findAllByRestaurantId(restaurantId, pageable))
                .map(item -> prepareRestaurantLinks(restaurantId, item));
    }

    @Override
    public ReviewResource get(Long restaurantId, Long id) {
        log.trace("list review request: {}, {}", restaurantId, id);

        Review entity = getReviewEntity(restaurantId, id);

        log.debug("review found: {}", entity);

        return prepareRestaurantLinks(restaurantId, mapper.to(entity));
    }

    @Override
    public ReviewResource update(Long restaurantId, Long id, ReviewResource resource) {
        log.trace("update review request for restaurantId: {}; id: {}; resource: {}", restaurantId, id, resource);

        Restaurant restaurantEntity = getRestaurantEntity(restaurantId);
        log.debug("restaurant found: {}", restaurantEntity);

        Review entity = getReviewEntity(restaurantId, id);
        log.debug("review found: {}", entity);

        // previous star count is used to find star change to aply restaurant review stats
        short previousStarCount = entity.getStarCount();

        // do not allow updating id
        resource.setId(id);

        mapper.mapFrom(entity, resource);

        entity.setReviewTime(Instant.now());
        entity.setRestaurant(restaurantEntity);

        repository.save(entity);

        // compute restaurant review stats (average review, etc.)
        reviewStatsService.computeReview(previousStarCount, entity, 0);
        log.debug("review ({}) computed for restaurant: {}", entity.getId(), restaurantEntity.getId());

        return get(restaurantId, resource.getId());
    }

    @Override
    public OwnerReplyResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource resource) {
        log.trace("update owner reply request for restaurantId: {}; id: {}; resource: {}", restaurantId, id, resource);

        Review entity = getReviewEntity(restaurantId, id);

        UserRole role = authenticationService.getCurrentUser().getRole();

        if (role == UserRole.OWNER && !Objects.equals(
                entity.getRestaurant().getOwner().getId(),
                authenticationService.getCurrentUser().getId())) {
            throw new AuthorizationServiceException("owner user cannot reply to restaurant: " + restaurantId);
        }

        // locate owner reply
        OwnerReply ownerReply = entity.getOwnerReply();
        if (ownerReply == null) {
            ownerReply = new OwnerReply();
        }

        ownerReplyMapper.mapFrom(ownerReply, resource);

        // back reference
        ownerReply.setReview(entity);

        ownerReplyRepository.save(ownerReply);

        log.debug("review updated: {}", entity);

        return ownerReplyMapper.to(ownerReply).add(
                linkTo(methodOn(ReviewController.class).get(restaurantId, entity.getId()))
                        .withRel(REVIEW),
                linkTo(methodOn(RestaurantController.class).get(restaurantId))
                        .withRel(RESTAURANT)
        );
    }

    @Override
    public void delete(Long restaurantId, Long id) {
        log.trace("delete review request for restaurantId: {}; id: {}", restaurantId, id);
        Review entity = getReviewEntity(restaurantId, id);
        log.debug("review found: {}", entity);

        repository.delete(entity);

        log.debug("review deleted: {}", entity);

        short previousStarCount = entity.getStarCount();
        entity.setStarCount((short) 0);

        reviewStatsService.computeReview(previousStarCount, entity, -1);
        log.debug("review ({}) computed for restaurant: {}", entity.getId(), restaurantId);
    }

    private ReviewResource prepareRestaurantLinks(long restaurantId, ReviewResource item) {
        return item.add(
                linkTo(methodOn(ReviewController.class).get(restaurantId, item.getId()))
                        .withSelfRel(),
                linkTo(methodOn(RestaurantController.class).get(restaurantId))
                        .withRel(RESTAURANT)
        );
    }

    private Review getReviewEntity(Long restaurantId, Long id) {
        log.debug("searching review from database with restaurantId: {}, id: {}", restaurantId, id);

        return repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("review not found with id: " + id));
    }

    private Restaurant getRestaurantEntity(Long restaurantId) {
        log.trace("searching restaurant with id: {}", restaurantId);

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + restaurantId));
    }
}

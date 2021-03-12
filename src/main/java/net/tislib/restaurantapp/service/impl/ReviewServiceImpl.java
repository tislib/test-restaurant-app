package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.controller.RestaurantController;
import net.tislib.restaurantapp.controller.ReviewController;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
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
import org.springframework.security.access.AccessDeniedException;
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
        Restaurant restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + restaurantId));

        Review entity = mapper.from(resource);

        entity.setId(null);
        entity.setReviewTime(Instant.now());
        entity.setRestaurant(restaurantEntity);
        entity.setUser(authenticationService.getCurrentUser());

        repository.save(entity);

        reviewStatsService.computeReview((short) 0, entity, 1);

        return get(restaurantId, entity.getId());
    }

    @Override
    public PageContainer<ReviewResource> list(Long restaurantId, BigDecimal rating, Pageable pageable) {
        return mapper.mapPage(repository.findAllByRestaurantId(restaurantId, pageable))
                .map(item -> prepareRestaurantLinks(restaurantId, item));
    }

    @Override
    public ReviewResource get(Long restaurantId, Long id) {
        Review entity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("review not found with id: " + id));

        return prepareRestaurantLinks(restaurantId, mapper.to(entity));
    }

    @Override
    public ReviewResource update(Long restaurantId, Long id, ReviewResource resource) {
        Restaurant restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + restaurantId));

        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        short previousStarCount = existingEntity.getStarCount();

        // do not allow updating id
        resource.setId(id);
        resource.setReviewTime(Instant.now());

        mapper.mapFrom(existingEntity, resource);

        existingEntity.setReviewTime(Instant.now());
        existingEntity.setRestaurant(restaurantEntity);

        repository.save(existingEntity);

        reviewStatsService.computeReview(previousStarCount, existingEntity, 0);

        return get(restaurantId, resource.getId());
    }

    @Override
    public OwnerReplyResource updateOwnerReply(Long restaurantId, Long id, OwnerReplyResource resource) {
        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        UserRole role = authenticationService.getCurrentUser().getRole();

        if (role == UserRole.OWNER && !Objects.equals(
                existingEntity.getRestaurant().getOwner().getId(),
                authenticationService.getCurrentUser().getId())) {
            throw new AuthorizationServiceException("owner user cannot reply to restaurant: " + restaurantId);
        }

        // locate owner reply
        OwnerReply ownerReply = existingEntity.getOwnerReply();
        if (ownerReply == null) {
            ownerReply = new OwnerReply();
        }

        ownerReplyMapper.mapFrom(ownerReply, resource);

        // back reference
        ownerReply.setReview(existingEntity);

        ownerReplyRepository.save(ownerReply);

        return ownerReplyMapper.to(ownerReply).add(
                linkTo(methodOn(ReviewController.class).get(restaurantId, existingEntity.getId()))
                        .withRel(REVIEW),
                linkTo(methodOn(RestaurantController.class).get(restaurantId))
                        .withRel(RESTAURANT)
        );
    }

    @Override
    public void delete(Long restaurantId, Long id) {
        Review existingEntity = repository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(() -> new EntityNotFoundException("review not found with id: " + id));

        repository.delete(existingEntity);

        short previousStarCount = existingEntity.getStarCount();
        existingEntity.setStarCount((short) 0);

        reviewStatsService.computeReview(previousStarCount, existingEntity, -1);
    }

    private ReviewResource prepareRestaurantLinks(long restaurantId, ReviewResource item) {
        return item.add(
                linkTo(methodOn(ReviewController.class).get(restaurantId, item.getId()))
                        .withSelfRel(),
                linkTo(methodOn(RestaurantController.class).get(restaurantId))
                        .withRel(RESTAURANT)
        );
    }
}

package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.controller.RestaurantController;
import net.tislib.restaurantapp.controller.ReviewController;
import net.tislib.restaurantapp.controller.UserController;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.data.mapper.RestaurantMapper;
import net.tislib.restaurantapp.exception.InvalidFieldException;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.UserRole;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsRepository;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    public static final String REVIEWS = "reviews";
    public static final String USER_NOT_EXISTS_MESSAGE = "user not exists";
    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;
    private final RestaurantReviewStatsRepository reviewStatsRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    public RestaurantResource create(final RestaurantResource resource) {
        Restaurant entity = mapper.from(resource);
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();

        entity.setId(null);

        // if user is not admin, sent owner as current user
        if (authenticationService.getCurrentUser().getRole() != UserRole.ADMIN) {
            entity.setOwner(authenticationService.getCurrentUser());
        } else {
            // check owner user exists
            if (resource.getOwner().getId() == null || userRepository.findById(entity.getOwner().getId()).isEmpty()) {
                throw new InvalidFieldException(RestaurantResource.Fields.owner, USER_NOT_EXISTS_MESSAGE);
            }
        }

        repository.save(entity);

        reviewStats.setRestaurant(entity);
        reviewStatsRepository.save(reviewStats);

        return get(entity.getId());
    }

    @Override
    public PageContainer<RestaurantResource> list(BigDecimal rating, Long ownerId, Pageable pageable) {
        Page<Restaurant> page = repository.findAll(pageable);

        return mapper.mapPage(page)
                .map(this::prepareRestaurantLinks);
    }

    @Override
    public RestaurantResource get(Long id) {
        Restaurant entity = getEntity(id);

        return prepareRestaurantLinks(mapper.to(entity));
    }

    @Override
    public RestaurantResource update(Long id, RestaurantResource resource) {
        Restaurant existingEntity = getEntity(id);
        RestaurantResource existingResource = mapper.to(existingEntity);

        resource.setId(id);

        // if user is not admin, rollback owner changes
        Long userId = resource.getOwner().getId();
        if (authenticationService.getCurrentUser().getRole() != UserRole.ADMIN) {
            userId = existingResource.getOwner().getId();
        }

        if (userId == null) {
            throw new InvalidFieldException(RestaurantResource.Fields.owner, "user not set");
        }

        mapper.mapFrom(existingEntity, resource);

        User user = userRepository.findById(resource.getOwner().getId())
                .orElseThrow(() -> new InvalidFieldException("owner", USER_NOT_EXISTS_MESSAGE));

        existingEntity.setOwner(user);

        repository.save(existingEntity);

        return get(resource.getId());
    }

    @Override
    public void delete(Long id) {
        Restaurant entity = getEntity(id);

        repository.delete(entity);
    }

    private Restaurant getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));
    }

    private RestaurantResource prepareRestaurantLinks(RestaurantResource item) {
        item.getOwner().add(linkTo(methodOn(UserController.class)
                .get(item.getOwner().getId()))
                .withSelfRel()
        );

        return item.add(
                linkTo(methodOn(RestaurantController.class).get(item.getId()))
                        .withSelfRel(),
                linkTo(methodOn(ReviewController.class).list(item.getId(), null, 0, 25))
                        .withRel(REVIEWS)
        );
    }

}

package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.data.mapper.RestaurantMapper;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsRepository;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;
    private final RestaurantReviewStatsRepository reviewStatsRepository;

    public RestaurantResource create(final RestaurantResource resource) {
        Restaurant entity = mapper.from(resource);
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();

        entity.setId(null);
        repository.save(entity);

        reviewStats.setRestaurant(entity);
        reviewStatsRepository.save(reviewStats);

        return get(entity.getId());
    }

    @Override
    public PageContainer<RestaurantResource> list(BigDecimal rating, Long ownerId, Pageable pageable) {
        return mapper.mapPage(repository.findAll(pageable));
    }

    @Override
    public RestaurantResource get(Long id) {
        Restaurant entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        RestaurantReviewStats reviewStats = reviewStatsRepository.findByRestaurantId(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant review stats not found with restaurant id: " + id));

        RestaurantResource resource = mapper.to(entity);

        mapper.mapReviews(resource, reviewStats);

        return resource;
    }

    @Override
    public RestaurantResource update(Long id, RestaurantResource resource) {
        Restaurant existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        resource.setId(id);
        mapper.mapFrom(existingEntity, resource);

        repository.save(existingEntity);

        return get(resource.getId());
    }

    @Override
    public void delete(Long id) {
        Restaurant entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found with id: " + id));

        repository.delete(entity);
    }

}

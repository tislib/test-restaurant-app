package net.tislib.restaurantapp.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, Long> {
}

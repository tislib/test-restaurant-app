package net.tislib.restaurantapp.model.repository;

import net.tislib.restaurantapp.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByRestaurantIdAndId(long restaurantId, long id);

    Optional<Review> findFirstByRestaurantIdOrderByStarCountAsc(Long id);

    Optional<Review> findFirstByRestaurantIdOrderByStarCountDesc(Long id);

    List<Review> findByComputed(boolean computed);

    Page<Review> findAllByOrderByReviewTimeDesc(Pageable pageable);
}

INSERT INTO "user" (id, email, password, "role", "full_name")
VALUES (101, 'user@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'REGULAR', null),
       (102, 'owner@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'OWNER', null),
       (103, 'owner2@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'OWNER', null),
       (104, 'admin@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN', null),

       (105, 'testuser1@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN',
        'testuser1'),
       (106, 'testuser2@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN',
        'testuser2'),
       (107, 'testuser3@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN',
        'testuser3');


INSERT INTO "restaurant" (id, name, owner_id)
VALUES (101, 'restaurant1', 102),
       (102, 'restaurant2', 102),
       (103, 'restaurant3', 103),
       (104, 'restaurant4', 103);

INSERT INTO "restaurant_review_stats" (restaurant_id, rating_average, rating_sum, rating_count,
                                       highest_rated_review_id, lowest_rated_review_id, version)
VALUES (101, 3, 9, 3, null, null, 1),
       (102, 0, 0, 0, null, null, 1),
       (103, 0, 0, 0, null, null, 1),
       (104, 0, 0, 0, null, null, 1);

INSERT INTO "review" (id, user_id, restaurant_id, star_count, comment, review_time, date_of_visit, computed)
VALUES (101, 101, 101, 3, 'test-comment-123', now(), now(), true),
       (102, 101, 101, 3, 'test-comment-123', now(), now(), true),
       (103, 101, 101, 3, 'test-comment-123', now(), now(), true);


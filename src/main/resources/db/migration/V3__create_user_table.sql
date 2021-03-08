CREATE TABLE "restaurant_review_stats"
(
    id            BIGSERIAL,
    restaurant_id int8
);

CREATE UNIQUE INDEX restaurant_review_stats_restaurant_uniq on "restaurant_review_stats" (restaurant_id);

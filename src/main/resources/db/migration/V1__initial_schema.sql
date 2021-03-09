CREATE TABLE "user"
(
    id       BIGSERIAL PRIMARY KEY,
    email    varchar(320),
    role     varchar(32),
    password varchar(255)
);

CREATE TABLE "restaurant"
(
    id       BIGSERIAL PRIMARY KEY,
    name     varchar(64),
    owner_id int8 NOT NULL,

    CONSTRAINT fk_restaurant_owner_id
        FOREIGN KEY (owner_id)
            REFERENCES "user" (id)
);

CREATE TABLE "review"
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       int8         NOT NULL,
    restaurant_id int8         NOT NULL,
    star_count    int2         NOT NULL,
    comment       varchar(255) NOT NULL,
    review_time   timestamp    NOT NULL,
    date_of_visit date         NOT NULL,

    CONSTRAINT fk_review_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (id),
    CONSTRAINT fk_review_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES "restaurant" (id)
);

CREATE TABLE "owner_reply"
(
    id        BIGSERIAL PRIMARY KEY,
    review_id int8         NOT NULL,
    comment   varchar(255) NOT NULL,

    CONSTRAINT fk_owner_reply_review_id
        FOREIGN KEY (review_id)
            REFERENCES "review" (id)
);

CREATE TABLE "restaurant_review_stats"
(
    id                      BIGSERIAL PRIMARY KEY,
    restaurant_id           int8,
    rating_average          int8 NOT NULL,
    rating_sum              int8 NOT NULL,
    rating_count            int4 NOT NULL,
    highest_rated_review_id int8 NOT NULL,
    lowest_rated_review_id  int8 NOT NULL,


    CONSTRAINT fk_restaurant_review_stats_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES "restaurant" (id),
    CONSTRAINT fk_restaurant_review_stats_highest_rater_review_id
        FOREIGN KEY (highest_rated_review_id)
            REFERENCES "review" (id),
    CONSTRAINT fk_restaurant_review_stats_lowest_rater_review_id
        FOREIGN KEY (lowest_rated_review_id)
            REFERENCES "review" (id)
);

CREATE INDEX restaurant_owner_id_idx on "restaurant" (owner_id);

CREATE UNIQUE INDEX user_email_unique on "user" (email);

CREATE UNIQUE INDEX restaurant_review_stats_restaurant_uniq on "restaurant_review_stats" (restaurant_id);
CREATE UNIQUE INDEX owner_reply_review_id_uniq on "owner_reply" (review_id);


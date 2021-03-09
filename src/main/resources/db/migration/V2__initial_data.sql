INSERT INTO "user" (email, password, "role")
VALUES ('user@app.com', '$2a$10$flDRghZ.GQ88S2NylLRj9OcOOqR2uy/YBuPyNQLFCPuwMd0sYhuja', 'REGULAR'),
       ('owner@app.com', '$2a$10$nBRykE0Jhk4tA/IXiQL60etE5QyZsfvm5FifZNrktXAI7G/opFQPK', 'OWNER'),
       ('owner2@app.com', '$2a$10$nBRykE0Jhk4tA/IXiQL60etE5QyZsfvm5FifZNrktXAI7G/opFQPK', 'OWNER'),
       ('admin@app.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN'),
       ('talehsmail@gmail.com', '$2a$10$HLeGQrRb5P6GIHtCRG.4gO5iVCODzCGl9qbGZc9ajKVXdE.DHBklO', 'ADMIN');

-- 1 -> user123
-- 2 -> owner123
-- 3 -> owner123
-- 4 -> admin123

INSERT INTO "restaurant" (name, owner_id)
VALUES ('restaurant1', 2),
       ('restaurant2', 2),
       ('restaurant3', 3),
       ('restaurant4', 3);

INSERT INTO "restaurant_review_stats" (restaurant_id, rating_average, rating_sum, rating_count,
                                       highest_rated_review_id, lowest_rated_review_id, version)
VALUES (1, 0, 0, 0, null, null, 1),
       (2, 0, 0, 0, null, null, 1),
       (3, 0, 0, 0, null, null, 1),
       (4, 0, 0, 0, null, null, 1);
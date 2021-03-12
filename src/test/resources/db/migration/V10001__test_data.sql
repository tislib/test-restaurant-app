INSERT INTO "user" (id, email, password, "role")
VALUES (101, 'user@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'REGULAR'),
       (102, 'owner@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'OWNER'),
       (103, 'owner2@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'OWNER'),
       (104, 'admin@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN');

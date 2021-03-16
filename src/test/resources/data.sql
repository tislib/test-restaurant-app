INSERT INTO "user" (id, email, password, "role")
VALUES (101, 'user@testapp.com', '$2a$10$flDRghZ.GQ88S2NylLRj9OcOOqR2uy/YBuPyNQLFCPuwMd0sYhuja', 'REGULAR'),
       (102, 'owner@testapp.com', '$2a$10$nBRykE0Jhk4tA/IXiQL60etE5QyZsfvm5FifZNrktXAI7G/opFQPK', 'OWNER'),
       (103, 'owner2@testapp.com', '$2a$10$nBRykE0Jhk4tA/IXiQL60etE5QyZsfvm5FifZNrktXAI7G/opFQPK', 'OWNER'),
       (104, 'admin@testapp.com', '$2a$10$Qa1ROlOgmXtvz6ZmHs7mlO9OKx9ARXql77RsuU/4xC2SxZrWnDJTm', 'ADMIN');

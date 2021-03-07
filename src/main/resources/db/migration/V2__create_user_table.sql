CREATE TABLE "user"
(
    id    BIGSERIAL,
    email varchar(320),
    password varchar(255)
);

CREATE UNIQUE INDEX user_email_unique on "user"(email);

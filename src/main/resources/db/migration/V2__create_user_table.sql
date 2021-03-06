CREATE TABLE "user"
(
    id    BIGSERIAL,
    email varchar(320)
);

CREATE UNIQUE INDEX user_email_unique on "user"(email);

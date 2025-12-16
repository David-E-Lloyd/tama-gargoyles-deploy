CREATE TABLE users (
  id bigserial PRIMARY KEY,
  username varchar(255) NOT NULL UNIQUE,
  rocks INT,
  bugs INT,
  mystery_food INT
);
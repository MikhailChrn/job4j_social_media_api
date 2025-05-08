CREATE TABLE files (
    id           SERIAL  PRIMARY KEY,
    post_id      INT     REFERENCES posts(id),
    title        TEXT    NOT NULL,
    path         TEXT    NOT NULL
);
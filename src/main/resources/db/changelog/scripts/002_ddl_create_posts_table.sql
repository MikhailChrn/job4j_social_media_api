CREATE TABLE posts (
    id             SERIAL     PRIMARY KEY,
    user_id        INT        NOT NULL   REFERENCES users(id),
    title          TEXT       NOT NULL,
    content        TEXT,
    created        TIMESTAMP  NOT NULL
);
CREATE TABLE messages (
    id             SERIAL     PRIMARY KEY,
    user_from_id   INT        NOT NULL   REFERENCES users(id),
    user_to_id     INT        NOT NULL   REFERENCES users(id),
    content        TEXT,
    created        TIMESTAMP  NOT NULL
);
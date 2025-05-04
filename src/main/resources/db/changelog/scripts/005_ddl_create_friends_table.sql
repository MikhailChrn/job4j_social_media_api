CREATE TABLE friends (
    id             SERIAL     PRIMARY KEY,
    user1_id       INT        NOT NULL   REFERENCES users(id),
    user2_id       INT        NOT NULL   REFERENCES users(id),
    created        TIMESTAMP  NOT NULL
);
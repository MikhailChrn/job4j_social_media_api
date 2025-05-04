CREATE TABLE subscribes (
    id             SERIAL     PRIMARY KEY,
    user_id        INT        NOT NULL   REFERENCES users(id),
    subscribe_id   INT        NOT NULL   REFERENCES users(id),
    created        TIMESTAMP  NOT NULL
);
CREATE TABLE friendships (
    id              SERIAL     PRIMARY KEY,
    user_hold_id    INT        NOT NULL   REFERENCES users(id),
    user_emit_id    INT        NOT NULL   REFERENCES users(id),
    created         TIMESTAMP  NOT NULL
);
CREATE TABLE user_balances (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL UNIQUE,
                               available_balance NUMERIC(19,4),
                               reserved_balance NUMERIC(19,4),
                               CONSTRAINT fk_user_balances_user
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);
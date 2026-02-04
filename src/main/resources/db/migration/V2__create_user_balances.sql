CREATE TABLE user_balances (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               available_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
                               reserved_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,

                               CONSTRAINT uq_user_balance_user UNIQUE (user_id),
                               CONSTRAINT fk_user_balance_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);
CREATE TABLE user_wallets (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              stock_id BIGINT NOT NULL,
                              available_amount INTEGER NOT NULL,
                              reserved_amount INTEGER NOT NULL,
                              CONSTRAINT fk_user_wallets_user
                                  FOREIGN KEY (user_id) REFERENCES users(id),
                              CONSTRAINT fk_user_wallets_stock
                                  FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
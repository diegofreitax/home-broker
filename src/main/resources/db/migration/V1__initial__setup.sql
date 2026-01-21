CREATE TABLE users (
                        id              BIGSERIAL PRIMARY KEY,
                        email           VARCHAR(255) NOT NULL UNIQUE,
                        password        VARCHAR(255) NOT NULL,
                        created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE stocks (
                        id               BIGINT PRIMARY KEY
);

CREATE TABLE orders (
                        id                  UUID PRIMARY KEY,
                        user_id             BIGINT NOT NULL,
                        stock_id            BIGINT NOT NULL,

                        type                VARCHAR(10) NOT NULL,
                        status              VARCHAR(20) NOT NULL,

                        unit_price          NUMERIC(19, 4) NOT NULL,

                        total_amount        INTEGER NOT NULL,
                        executed_amount     INTEGER NOT NULL DEFAULT 0,
                        remaining_amount    INTEGER NOT NULL,

                        created_at          TIMESTAMP NOT NULL DEFAULT now(),
                        updated_at          TIMESTAMP NOT NULL DEFAULT now(),

                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id)
                                REFERENCES users(id),

                        CONSTRAINT fk_orders_stock
                            FOREIGN KEY (stock_id)
                                REFERENCES stocks(id)
);

CREATE TABLE user_wallets (
                              id              BIGSERIAL PRIMARY KEY,
                              user_id         BIGINT NOT NULL,
                              stock_id        BIGINT NOT NULL,
                              amount          INTEGER NOT NULL,

                              CONSTRAINT uk_user_stock UNIQUE (user_id, stock_id),

                              CONSTRAINT fk_wallet_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id),

                              CONSTRAINT fk_wallet_stock
                                  FOREIGN KEY (stock_id)
                                      REFERENCES stocks(id)
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_stock_id ON orders(stock_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_type ON orders(type);

CREATE INDEX idx_wallet_user_id ON user_wallets(user_id);
CREATE INDEX idx_wallet_stock_id ON user_wallets(stock_id);
CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        stock_id BIGINT NOT NULL,

                        type VARCHAR(20) NOT NULL,
                        status VARCHAR(20) NOT NULL,

                        unit_price NUMERIC(19,4) NOT NULL,
                        total_amount INTEGER NOT NULL,
                        executed_amount INTEGER DEFAULT 0,
                        remaining_amount INTEGER,

                        created_at TIMESTAMP NOT NULL DEFAULT now(),
                        updated_at TIMESTAMP NOT NULL DEFAULT now(),

                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id) REFERENCES users(id),

                        CONSTRAINT fk_orders_stock
                            FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
CREATE TABLE trades (
                        id UUID PRIMARY KEY,

                        buy_order_id UUID NOT NULL,
                        sell_order_id UUID NOT NULL,

                        stock_id BIGINT NOT NULL,

                        quantity INTEGER NOT NULL,
                        price NUMERIC(19,4) NOT NULL,

                        executed_at TIMESTAMP NOT NULL,

                        CONSTRAINT fk_trades_buy_order
                            FOREIGN KEY (buy_order_id) REFERENCES orders(id),

                        CONSTRAINT fk_trades_sell_order
                            FOREIGN KEY (sell_order_id) REFERENCES orders(id),

                        CONSTRAINT fk_trades_stock
                            FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
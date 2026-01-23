CREATE TABLE trades (
                        id              UUID PRIMARY KEY,
                        buy_order_id    UUID NOT NULL,
                        sell_order_id   UUID NOT NULL,
                        stock_id        BIGINT NOT NULL,
                        quantity        INTEGER NOT NULL,
                        price           NUMERIC(19, 4) NOT NULL,
                        executed_at     TIMESTAMP NOT NULL DEFAULT now()
);
                        CREATE INDEX idx_trades_stock_id
                            ON trades(stock_id);

                        CREATE INDEX idx_trades_executed_at
                            ON trades(executed_at);



CREATE TABLE stock_prices (
                              stock_id        BIGINT PRIMARY KEY,
                              last_price      NUMERIC(19, 4) NOT NULL,
                              updated_at      TIMESTAMP NOT NULL DEFAULT now()
);
                              CREATE INDEX idx_stock_prices_updated_at
                                  ON stock_prices(updated_at);

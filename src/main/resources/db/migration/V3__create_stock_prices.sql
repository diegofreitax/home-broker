CREATE TABLE stock_prices (
                              stock_id BIGINT PRIMARY KEY,
                              last_price NUMERIC(19,4) NOT NULL,
                              updated_at TIMESTAMP NOT NULL,
                              CONSTRAINT fk_stock_prices_stock
                                  FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
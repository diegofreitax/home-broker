ALTER TABLE user_wallets
    ADD COLUMN available_amount INTEGER NOT NULL DEFAULT 0;

ALTER TABLE user_wallets
    ADD COLUMN reserved_amount INTEGER NOT NULL DEFAULT 0;

ALTER TABLE user_wallets
    ADD COLUMN available_amount INTEGER NOT NULL DEFAULT 0;

ALTER TABLE user_wallets
    ADD COLUMN reserved_amount INTEGER NOT NULL DEFAULT 0;

UPDATE user_wallets
SET available_amount = amount;

ALTER TABLE user_wallets
    DROP COLUMN amount;
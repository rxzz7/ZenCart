-- Existing seed verification rows are placeholders and contain duplicate/expired tokens.
DELETE FROM verification_tokens;

ALTER TABLE verification_tokens
    ADD COLUMN purpose VARCHAR(32) NOT NULL DEFAULT 'ACTIVATION';

ALTER TABLE verification_tokens
    MODIFY verify_token VARCHAR(255) NOT NULL;

ALTER TABLE credentials
    MODIFY user_id INT NOT NULL;

ALTER TABLE credentials
    ADD CONSTRAINT uk_credentials_user_id UNIQUE (user_id);

-- The original seed migration gives every user the same default email.
UPDATE users
SET email = CONCAT('legacy+', user_id, '@example.local')
WHERE email = 'springxyzabcboot@gmail.com';

ALTER TABLE users
    MODIFY email VARCHAR(255) NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);

ALTER TABLE users
    MODIFY first_name VARCHAR(255) NOT NULL,
    MODIFY last_name VARCHAR(255) NOT NULL;

ALTER TABLE address
    MODIFY user_id INT NOT NULL,
    MODIFY full_address VARCHAR(255) NOT NULL,
    MODIFY postal_code VARCHAR(255) NOT NULL,
    MODIFY city VARCHAR(255) NOT NULL;

ALTER TABLE credentials
    MODIFY username VARCHAR(255) NOT NULL,
    MODIFY password VARCHAR(255) NOT NULL,
    MODIFY role VARCHAR(255) NOT NULL,
    MODIFY is_enabled BOOLEAN NOT NULL,
    MODIFY is_account_non_expired BOOLEAN NOT NULL,
    MODIFY is_account_non_locked BOOLEAN NOT NULL,
    MODIFY is_credentials_non_expired BOOLEAN NOT NULL;

CREATE TABLE refresh_tokens (
    refresh_token_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    credential_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_refresh_tokens_token UNIQUE (token),
    CONSTRAINT fk_refresh_tokens_credential
        FOREIGN KEY (credential_id) REFERENCES credentials(credential_id)
        ON DELETE CASCADE
);
CREATE INDEX idx_refresh_tokens_credential_id ON refresh_tokens(credential_id);

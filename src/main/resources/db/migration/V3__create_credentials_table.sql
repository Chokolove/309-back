CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE credentials (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  user_id UUID NOT NULL,

  type VARCHAR(50) NOT NULL,
  issuer VARCHAR(255) NOT NULL,
  license_number VARCHAR(255) NOT NULL,
  expiry_date DATE,

  status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_credentials_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);
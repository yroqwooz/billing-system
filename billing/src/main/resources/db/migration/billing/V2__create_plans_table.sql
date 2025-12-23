
CREATE TABLE plans (
    -- Primary Key
                       id UUID PRIMARY KEY,

    -- Basic info
                       name VARCHAR(255) NOT NULL,
                       status VARCHAR(20) NOT NULL,

    -- Duration (храним как Period: years, months, days)
                       duration_years INT NOT NULL DEFAULT 0,
                       duration_months INT NOT NULL DEFAULT 0,
                       duration_days INT NOT NULL DEFAULT 0,

    -- Price (Money value object)
                       price_amount NUMERIC(19, 2) NOT NULL,
                       price_currency VARCHAR(3) NOT NULL,  -- ISO 4217 currency code (USD, EUR, etc.)

    -- Optimistic locking
                       version BIGINT NOT NULL DEFAULT 0,

    -- Audit fields (опционально, но рекомендуется)
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                       CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'OBSOLETE')),
                       CONSTRAINT chk_price_positive CHECK (price_amount >= 0),
                       CONSTRAINT chk_price_scale CHECK (price_amount = ROUND(price_amount, 2)),
                       CONSTRAINT chk_duration_positive CHECK (
                           duration_years > 0 OR duration_months > 0 OR duration_days > 0
                           ),
                       CONSTRAINT chk_currency_code CHECK (LENGTH(price_currency) = 3)
);

-- Indexes for performance
CREATE INDEX idx_plans_status ON plans(status);
CREATE INDEX idx_plans_name ON plans(name);

-- Comments for documentation
COMMENT ON TABLE plans IS 'Billing plans with pricing and duration';
COMMENT ON COLUMN plans.version IS 'Optimistic locking version';
COMMENT ON COLUMN plans.status IS 'Plan lifecycle status: ACTIVE, INACTIVE, OBSOLETE';
COMMENT ON COLUMN plans.duration_years IS 'Plan duration - years component';
COMMENT ON COLUMN plans.duration_months IS 'Plan duration - months component';
COMMENT ON COLUMN plans.duration_days IS 'Plan duration - days component';
COMMENT ON COLUMN plans.price_amount IS 'Plan price amount with 2 decimal places';
COMMENT ON COLUMN plans.price_currency IS 'ISO 4217 currency code (e.g., USD, EUR)';
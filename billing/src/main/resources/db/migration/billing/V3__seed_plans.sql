-- V3__seed_plans.sql

INSERT INTO plans (id, name, status, duration_years, duration_months, duration_days, price_amount, price_currency, version)
VALUES
    -- Monthly plans
    ('11111111-1111-1111-1111-111111111111', 'Basic Monthly', 'ACTIVE', 0, 1, 0, 9.99, 'USD', 0),
    ('22222222-2222-2222-2222-222222222222', 'Pro Monthly', 'ACTIVE', 0, 1, 0, 19.99, 'USD', 0),
    ('33333333-3333-3333-3333-333333333333', 'Enterprise Monthly', 'ACTIVE', 0, 1, 0, 49.99, 'USD', 0),

    -- Yearly plans (with discount)
    ('44444444-4444-4444-4444-444444444444', 'Basic Yearly', 'ACTIVE', 1, 0, 0, 99.99, 'USD', 0),
    ('55555555-5555-5555-5555-555555555555', 'Pro Yearly', 'ACTIVE', 1, 0, 0, 199.99, 'USD', 0),
    ('66666666-6666-6666-6666-666666666666', 'Enterprise Yearly', 'ACTIVE', 1, 0, 0, 499.99, 'USD', 0),

    -- Trial plan
    ('77777777-7777-7777-7777-777777777777', 'Free Trial', 'ACTIVE', 0, 0, 14, 0.00, 'USD', 0);
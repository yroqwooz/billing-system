DO $$
BEGIN
    IF version() LIKE '%PostgreSQL%' THEN

DROP INDEX IF EXISTS idx_subscriptions_end_date;

CREATE INDEX idx_subscriptions_end_date_active
    ON subscriptions(end_date)
    WHERE status = 'ACTIVE';

CREATE INDEX idx_subscriptions_expired_active
    ON subscriptions(end_date, user_id)
    WHERE status = 'ACTIVE';

RAISE NOTICE 'PostgreSQL optimizations applied successfully';
END IF;
END $$;
CREATE TABLE subscriptions (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL,
                               plan_id UUID NOT NULL,
                               status VARCHAR(20) NOT NULL,
                               start_date DATE NOT NULL,
                               end_date DATE NOT NULL,
                               version BIGINT NOT NULL DEFAULT 0,

                               CONSTRAINT chk_status CHECK (status IN ('CREATED', 'ACTIVE', 'CANCELLED', 'EXPIRED')),
                               CONSTRAINT chk_dates CHECK (end_date >= start_date)
);

CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_end_date ON subscriptions(end_date) WHERE status = 'ACTIVE';

COMMENT ON TABLE subscriptions IS 'User subscriptions to billing plans';
COMMENT ON COLUMN subscriptions.version IS 'Optimistic locking version';
COMMENT ON COLUMN subscriptions.status IS 'Subscription lifecycle status';